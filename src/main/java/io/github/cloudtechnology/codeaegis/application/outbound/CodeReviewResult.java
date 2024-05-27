package io.github.cloudtechnology.codeaegis.application.outbound;

public record CodeReviewResult(String fileName, Boolean hasIssues, String issues, String beforeModification, String afterModification, String suggestions) {
    
}
