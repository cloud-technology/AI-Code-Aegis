package io.github.cloudtechnology.codeaegis.application;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.core.env.Environment;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.standard.AbstractShellComponent;

import io.github.cloudtechnology.codeaegis.application.outbound.AzureDevOpsClient;
import io.github.cloudtechnology.codeaegis.application.outbound.CodeReviewResult;
import io.github.cloudtechnology.codeaegis.utility.DirectoryExplorer;
import io.github.cloudtechnology.codeaegis.utility.FileContentReader;
import io.github.cloudtechnology.codeaegis.utility.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Command
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CodeReviewAgent extends AbstractShellComponent {

    private final Environment environment;

    @Command(command = { "codeReview" })
    public void codeReview() {
        System.out.println("Code Review Agent Generator");

        String email = environment.getProperty("Build.RequestedForEmail");
        String orgUri = environment.getProperty("System.CollectionUri");
        String teamProject = environment.getProperty("System.TeamProject");
        String personalAccessToken = environment.getProperty("devops_pat");

        log.info("orgUri={}, teamProject={}, email={}", orgUri, teamProject, email);

        Set<String> excludedDirectories = Set.of(".git", ".devcontainer", ".gradle", ".history", ".vscode", "build",
                "gradle", "config", "static");
        Set<String> excludedFiles = Set.of(".gitignore", "compose.yaml", "gradlew", "gradlew.bat", "HELP.md",
                "LICENSE", "README.md", "settings.gradle", ".DS_Store", "build.gradle");
        DirectoryExplorer explorer = new DirectoryExplorer();
        explorer.setExcludedDirectories(excludedDirectories);
        explorer.setExcludedFiles(excludedFiles);
        List<FileInfo> fileInfoList = explorer.listAllFilesAndDirectories();

        for (FileInfo fileInfo : fileInfoList) {
            if (!fileInfo.isDirectory()) {
                try {
                    String content = FileContentReader.readFileContent(Path.of(fileInfo.getAbsolutePath()));
                    fileInfo.setContent(content);
                } catch (Exception e) {
                    log.error("Failed to read file content: {}", fileInfo.getAbsolutePath(), e);
                }
            }
        }
        log.info("provider={}", environment.getProperty("ai.provider"));
        String apikey = environment.getProperty("ai.key");
        String model = environment.getProperty("ai.model");
        var openAiApi = new OpenAiApi(apikey);

        var openAiChatOptions = OpenAiChatOptions.builder()
                .withModel(model)
                .withTemperature(0.5f)
                .build();
        var chatModel = new OpenAiChatModel(openAiApi, openAiChatOptions);

        ChatClient chatClient = ChatClient.create(chatModel);

        var userPromptTemplate = """
                你是一位資深 java 開發工程師, 現在要做 code review,
                這是一個 Gradle Spring Boot 專案, 主程式 @SpringBootApplication 不用檢查。

                以下是 codereview 原則:
                請依照 Clean Code 原則來設計, 提供程式碼改善的建議, 提供重點說明即可。
                當邏輯較為複雜要有適當的註解輔助說明, 以及適當的 log 與 javadoc

                要有以下欄位資訊, 請用繁體中文撰寫, 如果沒有資料就填入空白
                hasIssues: 是否有問題, 沒有問題設為 false
                fileName: 檔名
                issues: 問題點簡短描述
                suggestions: 改善建議說明
                beforeModification: 修改前程式碼片段範例
                afterModification: 修改後程式碼片段範例

                程式碼:
                filename: {fileName}
                relativePath: {relativePath}
                ``` java
                {codeContent}
                ```
                """;
        AzureDevOpsClient azureDevOpsClient = new AzureDevOpsClient(orgUri, teamProject, personalAccessToken);
        for (FileInfo fileInfo : fileInfoList) {
            if (!fileInfo.isDirectory()) {
                // Prompt prompt = promptTemplate.create(Map.of("codeContent",
                // fileInfo.getContent()));
                CodeReviewResult codeReviewResult = null;
                try {
                    codeReviewResult = chatClient.prompt()
                            .user(userSpec -> userSpec
                                    .text(userPromptTemplate)
                                    .param("fileName", fileInfo.getName())
                                    .param("relativePath", fileInfo.getRelativePath())
                                    .param("codeContent", fileInfo.getContent())
                                    .param("assignedTo", email))
                            .options(OpenAiChatOptions.builder()
                                    .withResponseFormat(
                                            new OpenAiApi.ChatCompletionRequest.ResponseFormat("json_object"))
                                    .build())
                            .call()
                            .entity(CodeReviewResult.class);
                    if (codeReviewResult.hasIssues()) {
                        azureDevOpsClient.createWorkItem(codeReviewResult, email);
                    }
                } catch (Exception e) {
                    log.error("Failed to read file content: {}", fileInfo.getAbsolutePath(), e);
                }
            }
        }
        log.info("review 完成");
    }
}
