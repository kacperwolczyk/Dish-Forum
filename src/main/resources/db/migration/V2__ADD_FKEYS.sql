ALTER TABLE RECIPE
    ADD FOREIGN KEY (DISH_ID) 
    REFERENCES DISH(ID);

ALTER TABLE COMMENT
    ADD FOREIGN KEY (RECIPE_ID) 
    REFERENCES RECIPE(ID);