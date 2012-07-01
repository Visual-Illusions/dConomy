package net.visualillusionsent.dconomy;


/**
 * dConomy User handling class
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class User {
    private String name;
    private boolean money, bank, joint, rank, create, auto, admin;
    private Misc misc;
    
    /**
     * Class constructor.
     * 
     * @param name      The name of the user.
     * @param money     Boolean value for if user can use money commands.
     * @param bank      Boolean value for if user can use bank commands.
     * @param joint     Boolean value for if user can use joint commands.
     * @param rank      Boolean value for if user can use rank commands.
     * @param create    Boolean value for if user can create joint accounts.
     * @param auto      Boolean value for if user can use pay forwarding.
     * @param admin     Boolean value for if user is a dConomy Admin.
     * @since   2.0
     */
    public User(String name, boolean money, boolean bank, boolean joint, boolean rank, boolean create, boolean auto, boolean admin, Misc misc){
        this.name = name;
        this.money = money;
        this.bank = bank;
        this.joint = joint;
        this.rank = rank;
        this.create = create;
        this.auto = auto;
        this.admin = admin;
        this.misc = misc;
    }
    
    /**
     * Return the user's name
     * 
     * @return name     The name of this user.
     * @since   2.0
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns if a user can use money commands
     * 
     * @return money    Boolean value for if user can use money commands.
     * @since   2.0
     */
    public boolean useMoney(){
        return money;
    }
    
    /**
     * Returns if a user can use bank commands
     * 
     * @return bank     Boolean value for if user can use bank commands.
     * @since   2.0
     */
    public boolean useBank(){
        return bank;
    }
    
    /**
     * Returns if a user can use joint commands
     * 
     * @return joint    Boolean value for if user can user joint commands.
     * @since   2.0
     */
    public boolean useJoint(){
        return joint;
    }
    
    /**
     * Returns if a user can use rank commands
     * 
     * @return rank     Boolean value for if use can use rank commands.
     * @since   2.0
     */
    public boolean canRank(){
        return rank;
    }
    
    /**
     * Returns if a user can create joint accounts
     * 
     * @return create   Boolean value for if user can create joint accounts.
     * @since   2.0
     */
    public boolean canCreate(){
        return create;
    }
    
    /**
     * Returns if a user can use money commands
     * 
     * @return auto     Boolean value for if user can use pay forwarding.
     * @since   2.0
     */
    public boolean canForward(){
        return auto;
    }
    
    /**
     * Returns if a user is a dConomy Admin
     * 
     * @return admin    Boolean value for if user is a dConomy Admin.
     * @since   2.0
     */
    public boolean isAdmin(){
        return admin;
    }
    
    public Misc getMisc(){
        return misc;
    }
}
