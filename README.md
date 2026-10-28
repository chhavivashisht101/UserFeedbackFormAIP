# User Feedback Form Application — AIP Project-I

**Student:** Chhavi Sharma  |  **UID:** 26MCI10155

**Technology:** HTML5, CSS3 (animations), JavaScript, Java Servlet (Jakarta Servlet 6), Maven, Apache Tomcat 10.1 / 11.

## Features
- Name, email, category, star rating and feedback fields.
- GET / POST switch on the form (same servlet handles both through `doGet()` and `doPost()`).
- Client-side validation (JavaScript) and server-side validation (servlet).
- HTML escaping of all user input (prevents XSS).
- Animated UI: gradient background, floating blobs, floating labels, animated star rating, loading button, animated success tick and confetti.
- Demonstrates client-server communication, `HttpServletRequest` and `HttpServletResponse`.

## Requirements
- JDK 17 or newer
- Apache Tomcat 10.1 or 11 (uses `jakarta.servlet`)
- Maven, or Eclipse IDE for Enterprise Java with Maven support

> For Tomcat 9 (or older) replace `jakarta.servlet` with `javax.servlet` in the servlet imports,
> use `javax.servlet-api 4.0.1` in `pom.xml`, and use the `javax` namespace / version 4.0 in `web.xml`.

## Run in Eclipse
1. Configure a JDK 17+ and Apache Tomcat 11 in Eclipse.
2. **File → Import → Maven → Existing Maven Projects** and select the folder containing `pom.xml`.
3. Right-click the project → **Maven → Update Project** (tick *Force Update*).
4. Right-click the project → **Run As → Run on Server** (choose Tomcat 11).
5. Open `http://localhost:8080/UserFeedbackFormAIP/`.

## Run with Maven and Tomcat
1. Run `mvn clean package` in the project root.
2. Copy `target/UserFeedbackFormAIP.war` into Tomcat's `webapps` directory.
3. Start Tomcat and open `http://localhost:8080/UserFeedbackFormAIP/`.

GET is included for syllabus demonstration; submitted values appear in the URL, so do not use GET for sensitive information.
This project displays feedback but does not store it permanently in a database.
