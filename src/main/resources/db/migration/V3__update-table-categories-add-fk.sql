alter table categories ADD CONSTRAINT fk_categories_on_books FOREIGN KEY(book_id) REFERENCES books(id);



