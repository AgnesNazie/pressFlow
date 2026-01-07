package se.lexicon.pressflow.dto;

public record AttachmentDto(Long id,
                            String fileName,
                            String fileType,
                            byte[] data) {
}
