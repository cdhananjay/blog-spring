package com.example.blog.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagRequest {
    @NotEmpty(message = "At least one tag is required.")
    @Size(max = 10, message = "Maximum of {max} tags can be created at once.")
    private Set<@NotBlank @Pattern(regexp = "^[A-Za-z0-9 -]+$", message = "Tag name can only contain letters, numbers, spaces and hyphen") String> tags;
}
