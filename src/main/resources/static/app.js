document.getElementById('bookForm').addEventListener('submit', onAddBook);
document.getElementById('authorForm').addEventListener('submit', onAddAuthor);
document.addEventListener('DOMContentLoaded', onLoadData);
document.addEventListener('DOMContentLoaded', function () {
    loadAuthors();
});

function onLoadData() {
    loadBooks();
    loadAuthors();
}

function loadBooks() {
    fetch("http://localhost:8080/api/books")
        .then(response => response.json())
        .then(books => {
            const booksContainer = document.getElementById('booksContainer');
            booksContainer.innerHTML = '';
            books.forEach(book => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${book.title}</td>
                    <td>${book.genres.join(', ')}</td>
                    <td>${book.publicationYear}</td>
                    <td>${book.price}</td>
                    <td>${book.isAvailable ? 'Yes' : 'No'}</td>
                    <td>${book.authorName}</td>
                    <td>${book.isbn}</td>
                    <td>
                        <button onclick="onEditBook(${book.id})">Edit</button>
                        <button onclick="onDeleteBook(${book.id})">Delete</button>
                    </td>
                `;

                booksContainer.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading books:', error));
}

function loadAuthors() {
    fetch("http://localhost:8080/api/authors")
        .then(response => response.json())
        .then(authors => {
            const authorsContainer = document.getElementById('authorsContainer');
            authorsContainer.innerHTML = '';
            authors.forEach(author => {
                const row = document.createElement('tr');

                row.innerHTML = `
                    <td>${author.name}</td>
                    <td>${author.dateOfBirth}</td>
                    <td>${author.country}</td>
                    <td>${author.rating}</td>
                    <td>${author.books.join(', ')}</td>
                `;

                authorsContainer.appendChild(row);
            });
        })
        .catch(error => console.error('Error loading authors:', error));
}

function onAddBook(event) {
    event.preventDefault();

    const book = {
        title: document.getElementById('title').value,
        genres: document.getElementById('genres').value.split(','),
        publicationYear: parseInt(document.getElementById('publicationYear').value),
        price: parseFloat(document.getElementById('price').value),
        isAvailable: document.getElementById('isAvailable').checked,
        authorName: document.getElementById('authorName').value,
        isbn: document.getElementById('isbn').value,
    };

    fetch("http://localhost:8080/api/books/create", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(book),
    })
        .then(response => {
            if (response.ok) {
                alert('Book added successfully!');
                document.getElementById('bookForm').reset();
                loadBooks();
            } else {
                alert('Failed to add book.');
            }
        })
        .catch(error => console.error('Error adding book:', error));
}
function onAddAuthor(event) {
    event.preventDefault();

    const author = {
        name: document.getElementById('authorNameInput').value,
        dateOfBirth: document.getElementById('dateOfBirth').value,
        country: document.getElementById('country').value,
        rating: parseFloat(document.getElementById('rating').value),
        books: document.getElementById('books').value.split(','),
    };

    fetch("http://localhost:8080/api/authors/create", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(author),
    })
        .then(response => {
            if (response.ok) {
                alert('Author added successfully!');
                document.getElementById('authorForm').reset();
                loadAuthors();
            } else {
                alert('Failed to add author.');
            }
        })
        .catch(error => console.error('Error adding author:', error));
}

function onDeleteBook(bookId) {
    fetch(`http://localhost:8080/api/books/${bookId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                alert('Book deleted successfully!');
                loadBooks();
            } else {
                alert('Failed to delete book.');
            }
        })
        .catch(error => console.error('Error deleting book:', error));
}

// Функция за редактиране на книга
function onUpdate(event) {
    // Вземи информацията за книгата и попълни формата
    fetch(`http://localhost:8080/api/books/${bookId}`)
        .then(response => response.json())
        .then(book => {
            // Попълни формата със стойностите на книгата
            document.getElementById('title').value = book.title;
            document.getElementById('genres').value = book.genres.join(',');
            document.getElementById('publicationYear').value = book.publicationYear;
            document.getElementById('price').value = book.price;
            document.getElementById('isAvailable').checked = book.isAvailable;
            document.getElementById('authorName').value = book.authorName;
            document.getElementById('isbn').value = book.isbn;

            // Променяме атрибута на формата, за да укажем, че това е режим на редактиране
            const form = document.getElementById('bookForm');
            form.dataset.mode = 'edit';  // Сетваме mode да е 'edit'
            form.action = `/api/books/update/${bookId}`; // Задаваме правилния URL за обновяване
        })
        .catch(error => {
            console.error('Error fetching book details:', error);
        });
}

    // Изпращане на PUT заявка към бекенда
    fetch(`http://localhost:8080/api/books/update/${bookId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(bookData), // Изпращаме новите данни
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(error => {
                    throw new Error(error);
                });
            }
            return response.json();
        })
        .then(updatedBook => {
            console.log('Book successfully updated:', updatedBook);
            alert('Book updated successfully!');
            loadBooks(); // Презареждаме списъка с книги
            resetForm();  // Изчистваме формата
        })
        .catch(error => {
            console.error('Error updating book:', error);
            alert(`Error updating book: ${error.message}`);
        });


// Функция за изчистване на формата
function resetForm() {
    document.getElementById('bookForm').reset(); // Нулираме всички стойности във формата
    const submitButton = document.querySelector('#bookForm button[type="submit"]');
    submitButton.textContent = 'Add Book';  // Връщаме текста на бутона на "Add Book"
    submitButton.dataset.action = 'add';  // Премахваме атрибута за update
}

