package com.mate.velkey.links.repository;

import com.mate.velkey.links.model.link.Link;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface LinkRepository extends CrudRepository<Link, String> {
}
