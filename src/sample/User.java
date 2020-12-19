package sample;

public class User {
    private String username;
    private String password;

    public static class Builder {
        private String username;
        private String password;

        public Builder username(String username) {
            this.username = username;

            return this;
        }

        public Builder password(String password) {
            this.password = password;

            return this;
        }

        public User build() {
            User user = new User();
            user.username = this.username;
            user.password = this.password;

            return user;
        }
    }

    private User() { }
}
