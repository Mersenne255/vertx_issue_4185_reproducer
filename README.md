### Description
Metric vertx_net_client_active_connections not being decreased
https://github.com/eclipse-vertx/vert.x/issues/#4185
 
### Steps
1. Run main class that starts vertx metrics server and http client
2. Http client sends one request and closes
3. Check metrics and see that net connections remain at 1