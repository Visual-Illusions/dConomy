package net.visualillusionsent.dconomy;

public class User {
    private String name;
    private boolean money, bank, joint, rank, create, auto, admin;
    
    public User(String name, boolean money, boolean bank, boolean joint, boolean rank, boolean create, boolean auto, boolean admin){
        this.name = name;
        this.money = money;
        this.bank = bank;
        this.joint = joint;
        this.rank = rank;
        this.create = create;
        this.auto = auto;
        this.admin = admin;
    }
    
    /**
     * Return the user's name
     * 
     * @return String name
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns if a user can use money commands
     * 
     * @return boolean true if can, false otherwise
     */
    public boolean useMoney(){
        return money;
    }
    
    /**
     * Returns if a user can use bank commands
     * 
     * @return boolean true if can, false otherwise
     */
    public boolean useBank(){
        return bank;
    }
    
    /**
     * Returns if a user can use joint commands
     * 
     * @return boolean true if can, false otherwise
     */
    public boolean useJoint(){
        return joint;
    }
    
    /**
     * Returns if a user can use rank commands
     * 
     * @return boolean true if can, false otherwise
     */
    public boolean canRank(){
        return rank || admin;
    }
    
    /**
     * Returns if a user can create joint accounts
     * 
     * @return boolean true if can, false otherwise
     */
    public boolean canCreate(){
        return create || admin;
    }
    
    /**
     * Returns if a user can use money commands
     * 
     * @return boolean true if can, false otherwise
     */
    public boolean canForward(){
        return auto || admin;
    }
    
    /**
     * Returns if a user is a dConomy Admin
     * 
     * @return boolean true if can, false otherwise
     */
    public boolean isAdmin(){
        return admin;
    }
}
