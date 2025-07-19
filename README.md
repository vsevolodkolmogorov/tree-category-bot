### 🏷️ Type: Interview Assignment

# Category Tree Bot

## 📌 Overview

**Category Tree Bot** is a Telegram bot that allows users to create, view, and delete a hierarchical category tree.  
The bot is built with **Spring Boot**, uses **PostgreSQL** for data storage, and applies the **Command** design pattern to handle commands.


## 🧩 Architecture

- Uses the **Command** design pattern to handle bot commands.
- Follows **SOLID** principles.
- Business logic is implemented in the **service layer**.
- Categories are stored in **PostgreSQL** as a hierarchical structure (parent → children).
  

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL**
- **TelegramBots (Java Telegram Bot API)**

## 🚀 Features

### ✅ **Add elements**
- **`/addElement <element name>`**  
  Adds a new root element.
- **`/addElement <parent name> <child name>`**  
  Adds a child element to the specified parent.
    - If the parent does not exist, an appropriate error message is displayed.

### ✅ **View category tree**
- **`/viewTree`**  
  Displays the entire category tree in a structured format:  
  - Parent
    - Child1
      - SubChild
    - Child2

### ✅ **Remove elements**
- **`/removeElement <element name>`**  
  Removes the specified element along with all its child elements.
- If the element does not exist, an appropriate error message is displayed.

### ✅ **Help**
- **`/help`**  
  Shows all available commands with short descriptions.


## ▶️ How to Run

### 1. Clone the repository
```bash
git clone https://github.com/vsevolodkolmogorov/category-tree-bot.git
```

### 2. Configure application properties

Edit application.yml or application.properties:

```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/category_bot
    username: your_user
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
bot:
  username: your_bot_username
  token: your_bot_token
```

### 3. Run the project and interact with the bot
