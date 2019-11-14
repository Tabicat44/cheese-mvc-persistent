package org.launchcode.models.data;

import org.launchcode.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
//import main.java.org.launchcode.models;


import javax.transaction.Transactional;


@Repository
@Transactional
public interface CategoryDao extends CrudRepository<Category, Integer> {
}
