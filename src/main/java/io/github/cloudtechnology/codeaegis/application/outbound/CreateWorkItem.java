package io.github.cloudtechnology.codeaegis.application.outbound;

/**
 * WorkItem 用於存儲 Azure DevOps 工作項目的基本資訊。
 *
 * @param title       工作項目的標題
 * @param description 工作項目的描述
 * @param assignedTo  工作項目分配給的用戶
 */
public record CreateWorkItem(String title, String description, String assignedTo) {
    
}
