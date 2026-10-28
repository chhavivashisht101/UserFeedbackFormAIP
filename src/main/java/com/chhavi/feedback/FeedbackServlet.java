package com.chhavi.feedback;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Handles the feedback form. Both doGet() and doPost() delegate to the same
 * processing method so that GET and POST can be demonstrated side by side.
 */
@WebServlet(name = "FeedbackServlet", urlPatterns = {"/feedback"})
public class FeedbackServlet extends HttpServlet {

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final String[] RATING_TEXT =
            {"", "Poor", "Fair", "Good", "Very good", "Excellent"};

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processFeedback(request, response, "GET");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processFeedback(request, response, "POST");
    }

    private void processFeedback(HttpServletRequest request, HttpServletResponse response, String method)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String name     = clean(request.getParameter("name"));
        String email    = clean(request.getParameter("email"));
        String category = clean(request.getParameter("category"));
        String feedback = clean(request.getParameter("feedback"));
        int rating      = parseRating(request.getParameter("rating"));

        if (category.isEmpty()) category = "General";

        PrintWriter out = response.getWriter();
        head(out);

        // ---------- server-side validation ----------
        String error = null;
        if (name.isEmpty() || email.isEmpty() || feedback.isEmpty()) {
            error = "Please enter your name, email address and feedback.";
        } else if (!EMAIL.matcher(email).matches()) {
            error = "The email address does not look valid.";
        } else if (rating < 1 || rating > 5) {
            error = "Please choose a star rating between 1 and 5.";
        } else if (name.length() > 80 || email.length() > 120 || feedback.length() > 2000) {
            error = "One of the fields is longer than allowed.";
        }

        if (error != null) {
            out.println("<div class='error-icon'>!</div>");
            out.println("<h1>Something is missing</h1>");
            out.println("<p class='lead'>" + escapeHtml(error) + "</p>");
            out.println("<a class='btn' href='index.html'>&larr; Go back to the form</a>");
            tail(out, false);
            return;
        }

        String ref = "FB-" + (1000 + new Random().nextInt(9000));
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

        out.println("<svg class='check' viewBox='0 0 52 52'><circle class='check-circle' cx='26' cy='26' r='24'/>"
                + "<path class='check-mark' d='M14 27 l8 8 l16 -17'/></svg>");
        out.println("<span class='tag'>Feedback received</span>");
        out.println("<h1>Thank you, " + escapeHtml(name) + "!</h1>");
        out.println("<p class='lead'>Your feedback was processed successfully by the Java Servlet.</p>");

        out.println("<div class='meta'>");
        out.println("<span class='pill'>Method: <b>" + method + "</b></span>");
        out.println("<span class='pill'>Ref: <b>" + ref + "</b></span>");
        out.println("<span class='pill'>" + escapeHtml(time) + "</span>");
        out.println("</div>");

        out.println("<dl class='details'>");
        out.println("<dt>Name</dt><dd>" + escapeHtml(name) + "</dd>");
        out.println("<dt>Email</dt><dd>" + escapeHtml(email) + "</dd>");
        out.println("<dt>Category</dt><dd>" + escapeHtml(category) + "</dd>");
        out.println("<dt>Rating</dt><dd><span class='stars-out'>" + stars(rating) + "</span> "
                + rating + "/5 &middot; " + RATING_TEXT[rating] + "</dd>");
        out.println("<dt>Feedback</dt><dd class='msg'>" + escapeHtml(feedback) + "</dd>");
        out.println("</dl>");

        out.println("<a class='btn' href='index.html'>&larr; Submit another response</a>");
        tail(out, true);
    }

    // ---------- helpers ----------
    private void head(PrintWriter out) {
        out.println("<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>Feedback Confirmation | AIP</title>");
        out.println("<link rel='stylesheet' href='style.css'></head>");
        out.println("<body class='result-page'>");
        out.println("<div class='bg'><span class='blob b1'></span><span class='blob b2'></span><span class='blob b3'></span></div>");
        out.println("<main class='container'><section class='card result-card'>");
    }

    private void tail(PrintWriter out, boolean success) {
        out.println("</section></main>");
        out.println("<div id='confetti' data-fire='" + success + "'></div>");
        out.println("<script src='script.js'></script></body></html>");
    }

    private String stars(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) sb.append(i <= rating ? "&#9733;" : "&#9734;");
        return sb.toString();
    }

    private int parseRating(String value) {
        try {
            return Integer.parseInt(value == null ? "" : value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    /** Escape user input before inserting it into an HTML response (prevents XSS). */
    private String escapeHtml(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                    .replace("\"", "&quot;").replace("'", "&#x27;");
    }
}
