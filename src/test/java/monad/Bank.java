package monad;

public class Bank {
    Branch branch;

    Bank(Branch branch) {
        this.branch = branch;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
