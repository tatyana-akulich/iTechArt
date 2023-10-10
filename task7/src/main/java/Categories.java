public enum Categories {
    Horror ("Horror"),
    Strategy("Strategy"),
    Software("Software"),
    Survival("Survival"),
    Anime("Anime"),
    Adventure ("Adventure"),
    Space("Space");
    private String title;

    Categories(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
