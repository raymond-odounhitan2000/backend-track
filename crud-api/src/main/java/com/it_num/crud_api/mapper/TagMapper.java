package com.it_num.crud_api.mapper;

import com.it_num.crud_api.domain.Tag;
import com.it_num.crud_api.dto.request.create.TagCreateRequest;
import com.it_num.crud_api.dto.request.update.TagUpdateRequest;
import com.it_num.crud_api.dto.response.TagResponse;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public Tag toEntity(TagCreateRequest request) {
        Tag tag = new Tag();
        tag.setName(request.name());
        tag.setColor(request.color());
        return tag;
    }

    public void updateEntity(Tag tag, TagUpdateRequest request) {
        tag.setName(request.name());
        tag.setColor(request.color());
    }

    public TagResponse toResponse(Tag tag) {
        return new TagResponse(
                tag.getPublicId(),
                tag.getName(),
                tag.getColor(),
                tag.getTasks().size(),
                tag.getCreatedAt()
        );
    }
}