package com.descenedigital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WelcomeController {

    @GetMapping("/")
    @ResponseBody
    public String welcome() {
        return """
                <html>
                <head>
                    <title>Advice API</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
                        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #333; text-align: center; }
                        .links { display: flex; justify-content: space-around; margin: 30px 0; }
                        .link-card { background: #007bff; color: white; padding: 20px; border-radius: 8px; text-decoration: none; text-align: center; flex: 1; margin: 0 10px; }
                        .link-card:hover { background: #0056b3; text-decoration: none; color: white; }
                        .info { background: #e9ecef; padding: 20px; border-radius: 8px; margin: 20px 0; }
                        .credentials { background: #d4edda; padding: 15px; border-radius: 8px; margin: 10px 0; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🧪 Welcome to Advice API</h1>
                        <p>A comprehensive REST API for managing advice with user authentication, rating system, and advanced features.</p>
                        
                        <div class="links">
                            <a href="/swagger-ui/index.html" class="link-card">
                                <h3>📚 API Documentation</h3>
                                <p>Explore all endpoints with Swagger UI</p>
                            </a>
                            <a href="/h2-console" class="link-card">
                                <h3>🗄️ Database Console</h3>
                                <p>Access H2 database console</p>
                            </a>
                        </div>
                        
                        <div class="info">
                            <h3>🚀 Quick Start</h3>
                            <p>1. Visit the <strong>API Documentation</strong> to explore all available endpoints</p>
                            <p>2. Use the <strong>Authentication</strong> endpoints to register or login</p>
                            <p>3. Get a JWT token and use it to access protected endpoints</p>
                        </div>
                        
                        <div class="credentials">
                            <h3>🔑 Sample Login Credentials</h3>
                            <p><strong>Admin User:</strong> username: <code>admin</code>, password: <code>password</code></p>
                            <p><strong>Regular User:</strong> username: <code>john_doe</code>, password: <code>password</code></p>
                        </div>
                        
                        <div class="info">
                            <h3>📋 Features Implemented</h3>
                            <ul>
                                <li>✅ JWT-based authentication & authorization</li>
                                <li>✅ User registration and role management</li>
                                <li>✅ Full CRUD operations for advice</li>
                                <li>✅ 5-star rating system with comments</li>
                                <li>✅ Advanced pagination, search & filtering</li>
                                <li>✅ Admin panel for user management</li>
                                <li>✅ Comprehensive API documentation</li>
                            </ul>
                        </div>
                    </div>
                </body>
                </html>
                """;
    }
}
