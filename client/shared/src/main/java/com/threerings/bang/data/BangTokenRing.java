//
// $Id$

package com.threerings.bang.data;

import com.threerings.crowd.data.TokenRing;

/**
 * Provides custom access controls.
 */
public class BangTokenRing extends TokenRing
{
	//DYLANADD: ADDING ADMIN
	public static final int ADMIN = 1;
    /** Indicates that the user is an "insider" and may get access to pre-release stuff. */
    public static final int INSIDER = 2;

    /** Indicates that the user is support personel. */
    public static final int SUPPORT = 4;

    /** Indicates that the user is a demo account. */
    public static final int DEMO = 8;

    /** Indicates that the user is anonymous. */
    public static final int ANONYMOUS = 16;

    /** Indicatest that the user is over 13. */
    public static final int OVER_13 = 32;

    /**
     * Constructs a token ring with the supplied set of tokens.
     */
    public BangTokenRing (int tokens)
    {
        super(tokens);
    }

	//DYLANADD: ADDING ADMIN
	public boolean isAdmin ()
    {
        return holdsToken(ADMIN);
    }
	
    /**
     * Convenience function for checking whether this ring holds the {@link #INSIDER} token.
     */
    public boolean isInsider ()
    {
        return holdsToken(INSIDER);
    }

    /**
     * Convenience function for checking whether this ring holds the {@link #SUPPORT} token.
     */
    public boolean isSupport ()
    {
        return holdsToken(SUPPORT);
    }

    /**
     * Convenience function for checking whether this ring holds the {@link #DEMO} token.
     */
    public boolean isDemo ()
    {
        return holdsToken(DEMO);
    }

    /**
     * Convenience function for checking whether this ring holds the {@link #ANONYMOUS} token.
     */
    public boolean isAnonymous ()
    {
        return holdsToken(ANONYMOUS);
    }

    /**
     * Convenience function for checking whether this ring holds the {@link #OVER_13} token.
     */
    public boolean isOver13 ()
    {
        return holdsToken(OVER_13);
    }
}
