package vjezbe.entitet;

public class Profesor extends Osoba{
    private String code, title;


    /** Kreira objekt profesora koji sadrzi sifru, ime, prezime i titulu
     * @param code
     * @param name
     * @param surname
     * @param title
     */
    public Profesor(Long id, String code, String name, String surname, String title) {
        super(id, name, surname);
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return  title + ' ' + getName() + ' ' + getSurname();
    }

    public static class Builder{
        private Long id;
        private String code, name, surname, title;

        public Builder withId(Long id){
            this.id = id;
            return this;
        }

        public Builder withCode(String code){
            this.code = code;
            return this;
        }

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withsurName(String surname){
            this.surname = surname;
            return this;
        }

        public Builder withTitle(String title){
            this.title = title;
            return this;
        }

        public Profesor build(){
            Profesor profesor = new Profesor(id, code, name, surname, title);
            return profesor;
        }

    }
}
