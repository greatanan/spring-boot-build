package com.greatanan.autoconfigue.repository;

import com.greatanan.autoconfigue.annotation.SecondLevelRepository;

@SecondLevelRepository(value = "myFirstLevelRepository") // Bean 名称
public class MyFirstLevelRepository {
}
