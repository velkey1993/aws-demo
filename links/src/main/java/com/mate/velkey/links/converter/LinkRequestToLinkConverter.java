package com.mate.velkey.links.converter;

import com.mate.velkey.links.model.image.Image;
import com.mate.velkey.links.model.link.Link;
import com.mate.velkey.links.model.link.LinkRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class LinkRequestToLinkConverter {

    private final String cloudFrontEndpointUrl;

    public LinkRequestToLinkConverter(@Value("${amazonProperties.cloudFront.endpointUrl}") String cloudFrontEndpointUrl) {
        this.cloudFrontEndpointUrl = cloudFrontEndpointUrl;
    }

    public Link convert(LinkRequest linkRequest, MultipartFile image, String imageFileName) {
        String imageFileUrl = cloudFrontEndpointUrl + "/" + imageFileName;
        return Link.builder()
                .withName(linkRequest.getName())
                .withUrl(linkRequest.getUrl())
                .withLabel(linkRequest.getLabel())
                .withImage(new Image(image.getOriginalFilename(), imageFileUrl))
                .build();
    }

    public Link convert(String id, LinkRequest linkRequest, MultipartFile image, String imageFileName) {
        Link link = convert(linkRequest, image, imageFileName);
        Objects.requireNonNull(link).setId(id);
        return null;
    }
}
