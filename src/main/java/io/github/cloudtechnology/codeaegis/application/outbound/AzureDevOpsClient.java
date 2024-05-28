package io.github.cloudtechnology.codeaegis.application.outbound;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * AzureDevOpsClient 用於與 Azure DevOps 進行交互，創建工作項目。
 */
@Slf4j
public class AzureDevOpsClient {

    // private final String organization;
    private final String orgUri; // System.CollectionUri: https://dev.azure.com/ooxx/
    private final String projectName;
    private final String personalAccessToken;
    private final RestTemplate restTemplate;

    /**
     * 構造 AzureDevOpsClient 物件。
     *
     * @param orgUri              Azure DevOps 組織 URI
     * @param projectName         Azure DevOps 專案名稱
     * @param personalAccessToken 個人訪問令牌
     */
    public AzureDevOpsClient(String orgUri, String projectName, String personalAccessToken) {
        this.orgUri = orgUri;
        this.projectName = projectName;
        this.personalAccessToken = personalAccessToken;
        this.restTemplate = new RestTemplate();
    }

    /**
     * 創建 Azure DevOps 工作項目。
     *
     * @param title       工作項目的標題
     * @param description 工作項目的描述
     */
    public void createWorkItem(CodeReviewResult codeReviewResult, String assignedTo) {
        String url = buildUrl();
        HttpHeaders headers = createHeaders();
        String title = codeReviewResult.issues();
        StringBuilder description = new StringBuilder();
        description.append("檔名:" + codeReviewResult.fileName() + "\n");
        description.append("改善建議:\n");
        description.append(codeReviewResult.suggestions() + "\n");
        description.append("調整前:\n");
        description.append(codeReviewResult.beforeModification() + "\n");
        description.append("調整後:\n");
        description.append(codeReviewResult.afterModification() + "\n");
        log.info("title: {}, description: {}", title, description.toString());

        List<Map<String, Object>> requestBody = createRequestBody(title, description.toString(), assignedTo);
        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("Response Body: " + response.getBody());
    }

    /**
     * 構建 Azure DevOps API URL。
     *
     * @return 構建的 URL 字符串
     */
    private String buildUrl() {
        return String.format(
                "%s/%s/_apis/wit/workitems/$%s?api-version=7.1-preview.3",
                orgUri,
                projectName,
                "task");
    }

    /**
     * 創建包含身份驗證和內容類型的 HTTP 標頭。
     *
     * @return 包含必要標頭的 HttpHeaders 物件
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json-patch+json");
        headers.set("Authorization", createAuthorizationHeader());
        return headers;
    }

    /**
     * 創建基本身份驗證標頭。
     *
     * @return 基本身份驗證標頭字符串
     */
    private String createAuthorizationHeader() {
        String auth = ":" + personalAccessToken;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }

    /**
     * 創建工作項目的請求正文。
     *
     * @param title       工作項目的標題
     * @param description 工作項目的描述
     * @return 包含請求正文的 List 物件
     */
    private List<Map<String, Object>> createRequestBody(String title, String description, String assignedTo) {
        Map<String, Object> titleField = Map.of(
                "op", "add",
                "path", "/fields/System.Title",
                "value", title);

        Map<String, Object> descriptionField = Map.of(
                "op", "add",
                "path", "/fields/System.Description",
                "value", description);

        Map<String, Object> assignedToField = Map.of(
                "op", "add",
                "path", "/fields/System.AssignedTo",
                "value", assignedTo);

        return List.of(titleField, descriptionField, assignedToField);
    }
}
