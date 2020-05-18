package com.mate.velkey.links.service;

import com.mate.velkey.links.converter.LinkRequestToLinkConverter;
import com.mate.velkey.links.exception.NoLinkFoundException;
import com.mate.velkey.links.model.link.Link;
import com.mate.velkey.links.model.link.LinkRequest;
import com.mate.velkey.links.repository.LinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final LinkRequestToLinkConverter requestConverter;
    private final ImageService imageService;

    public LinkService(LinkRepository linkRepository,
                       LinkRequestToLinkConverter requestConverter,
                       ImageService imageService) {
        this.linkRepository = linkRepository;
        this.requestConverter = requestConverter;
        this.imageService = imageService;
    }

    public Iterable<Link> getLinks() {
        return linkRepository.findAll();
    }

    public Link getLinkResponse(String id) {
        return getLink(id);
    }

    public Link createLink(LinkRequest linkRequest,
                           MultipartFile image) throws IOException {
        String imageFileName = imageService.createImage(image);
        Link link = requestConverter.convert(linkRequest, image, imageFileName);
        return linkRepository.save(link);
    }

    public Link updateLink(String id,
                           LinkRequest linkRequest,
                           MultipartFile image) throws IOException {
        String imageFileName = imageService.createImage(image);
        Link link = requestConverter.convert(id, linkRequest, image, imageFileName);
        return linkRepository.save(link);
    }

    public void deleteLink(String id) {
        Optional<Link> linkById = linkRepository.findById(id);
        if (linkById.isPresent()) {
            linkRepository.deleteById(id);
            imageService.deleteImage(linkById.get().getImage().getFileName());
        } else {
            throw new NoLinkFoundException();
        }
    }

    private Link getLink(String id) {
        return linkRepository.findById(id).orElseThrow(NoLinkFoundException::new);
    }
}
