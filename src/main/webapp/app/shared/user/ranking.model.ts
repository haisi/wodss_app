export class User {
    public login?: string;
    public points?: number;

    constructor(
        login?: string,
        points?: number
    ) {
        this.login = login ? login : null;
        this.points = points ? points : null;
    }
}
