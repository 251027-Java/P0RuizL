## 🚀 Running the Project

Follow these steps to set up the required **PostgreSQL** database and run the application.

### Prerequisites

Before starting, ensure you have the following installed on your system:

* **Docker:** Used to run the PostgreSQL database instance.
* **Apache Maven:** Used to build and run the Java project.
* **Java Development Kit (JDK):** Version 25 is used for this project.

---

### 1. Set Up the PostgreSQL Database

The application requires a PostgreSQL instance running and a specific database created within it.

* **Create and Run the Docker Container:**
    Use **Docker** to easily launch a PostgreSQL instance. Make sure the docker daemon is running and use the command below that sets up the container, names it `fantasy-postgres`, maps the default port $5432$, and sets up a sample user/password.

    ```bash
    docker run --name fantasy-postgres -e POSTGRES_USER=app_user -e POSTGRES_PASSWORD=my_secure_password -p 5432:5432 -d postgres
    ```
    * **Sample Credentials Used:**
        * **Username:** `app_user`
        * **Password:** `my_secure_password`
        * **Port:** $5432$ (Default)

* **Create the Database:**
    Once the container is running, you need to create the database named `fantasydb`. You can execute a command against the running container to do this:

    ```bash
    docker exec -it fantasy-postgres psql -U app_user -d postgres -c "CREATE DATABASE fantasydb;"
    ```
    **(Note: This command uses the sample username `app_user` and the container name `fantasy-postgres`.)**

---

### 2. Configure Database Connection

The application's source code needs to be updated with the correct database connection details.

* **Locate the File:**
    Navigate to the following configuration file:

    ```
    ./fantasy-tool/src/main/resources/META-INF/persistence.xml
    ```

* **Update Connection Properties:**
    Find the properties section for the JDBC connection and replace the placeholders with the database name, username, and password you used in **Step 1**.

    | Property | Replace Placeholder With |
    | :--- | :--- |
    | `jakarta.persistence.jdbc.url` | `jdbc:postgresql://localhost:5432/fantasydb` |
    | `jakarta.persistence.jdbc.user` | `app_user` |
    | `jakarta.persistence.jdbc.password` | `my_secure_password` |

    Your updated configuration should look like this:

    ```xml
    <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/fantasydb"/>
    <property name="jakarta.persistence.jdbc.user" value="app_user"/>
    <property name="jakarta.persistence.jdbc.password" value="my_secure_password"/>
    ```

---

### 3. Run the Application

With the database configured, you can now run the **Maven** project.

* **Execute the Project:**
    Ensure you are in the **main project directory** (the directory containing the `pom.xml` file) and run the following command:

    ```bash
    mvn exec:java
    ```

This command will compile and execute the application's main class.
