//
// $Id$

package com.threerings.bang.game.data.effect;

import java.awt.Point;
import java.awt.Rectangle;

import java.util.HashSet;

import com.samskivert.util.IntIntMap;

import com.threerings.bang.data.BonusConfig;

import com.threerings.bang.game.client.EffectHandler;
import com.threerings.bang.game.client.HoldHandler;
import com.threerings.bang.game.data.BangObject;
import com.threerings.bang.game.data.piece.Bonus;
import com.threerings.bang.game.data.piece.Unit;

import static com.threerings.bang.Log.log;

/**
 * Handles bonuses which can be held (therefor picked up and dropped).
 */
public class HoldEffect extends BonusEffect
{
    /** The identifier for the type of effect that we produce. */
    public static final String PICKED_UP_BONUS = "frontier_town/bonus/pickedup";

    /** The identifier for the type of effect that we produce. */
    public static final String DROPPED_BONUS = "frontier_town/bonus/dropped";

    /** If true, the piece in question is dropping a bonus; */
    public boolean dropping;

    /** The unit that caused us to drop our bonus (if any). */
    public int causerId = -1;

    /** The bonus to be dropped or null if no bonus is to be dropped. */
    public Bonus drop;

    /** The type of bonus being effected. */
    public String type;

    /**
     * Creates a hold effect configured to cause the specified unit to drop
     * their bonus. Returns null if a location for the bonus to fall could
     * not be found.
     *
     * @param causerId the piece id of the piece that caused this piece to drop
     * this bonus, used for animation sequencing.  If set to -2 then the piece will
     * lose the bonus points they earned for picking up the bonus.
     */
    public static HoldEffect dropBonus (
        BangObject bangobj, Unit unit, int causerId, String type)
    {
        Bonus bonus = Bonus.createBonus(BonusConfig.getConfig(type));
        if (bonus == null) {
            return null;
        }
        try {
            HoldEffect effect = (HoldEffect)Class.forName(
                    bonus.getConfig().effectClass).newInstance();
            effect.init(unit);
            effect.dropping = true;
            effect.causerId = causerId;
            effect.drop = bonus;
            effect.type = type;
            effect.drop.position(unit.x, unit.y);
            return effect;
        } catch (Exception e) {
            log.warning("Failed to instantiate effect class",
                        "class", bonus.getConfig().effectClass, e);
            return null;
        }
    }

    @Override // from Effect
    public Object clone ()
    {
        HoldEffect effect = (HoldEffect)super.clone();
        effect.drop = (drop == null ? null : (Bonus)drop.clone());
        return effect;
    }

    /**
     * Determines whether the given effect represents the dropping of
     * some kind of bonus.
     */
    public static boolean isDroppedEffect (String effect)
    {
        return _droppedEffects.contains(effect);
    }

    /**
     * Returns the identifier for the dropped bonus effect.
     */
    public String getDroppedEffect ()
    {
        return DROPPED_BONUS;
    }

    /**
     * Returns the identifier for the picked up bonus effect.
     */
    public String getPickedUpEffect ()
    {
        return PICKED_UP_BONUS;
    }

    @Override // from Effect
    public int[] getWaitPieces ()
    {
        if (causerId != -1) {
            return new int[] { causerId };
        }
        return super.getWaitPieces();
    }

    @Override // from Effect
    public int[] getAffectedPieces ()
    {
        if (drop != null) {
            return new int[] { pieceId, drop.pieceId, bonusId };
        }
        return new int[] { pieceId, bonusId };
    }

    @Override // from Effect
    public Rectangle[] getBounds (BangObject bangobj)
    {
        if (dropping && drop != null && drop.x != -1) {
            return new Rectangle[] { new Rectangle(drop.x, drop.y, 1, 1) };
        }
        return null;
    }

    @Override // from Effect
    public void prepare (BangObject bangobj, IntIntMap dammap)
    {
        Unit unit = (Unit)bangobj.pieces.get(pieceId);
        if (unit == null) {
            log.warning("Missing unit for hold effect", "id", pieceId);
            return;
        }

        if (!dropping) {
            // mark the target piece as holding now as they may have landed
            // next to an object which will also try to give them a holdable
            // bonus; we'll need to update their holding again in apply to
            // ensure that it happens on the client
            unit.holding = type;
            super.prepare(bangobj, dammap);

        } else if (drop != null) {
            Point spot = drop.getDropLocation(bangobj);
            if (spot == null) {
                drop.position(-1, -1);
            } else {
                drop.position(spot.x, spot.y);
                drop.assignPieceId(bangobj);
            }
            if (causerId == -2) {
                bangobj.grantBonusPoints(unit.owner, -getBonusPoints(bangobj));
            }
        }
    }

    @Override // from Effect
    public boolean isApplicable ()
    {
        return (drop == null || drop.x != -1);
    }

    @Override // from Effect
    public boolean apply (BangObject bangobj, Observer obs)
    {
        super.apply(bangobj, obs);

        Unit unit = (Unit)bangobj.pieces.get(pieceId);
        if (unit != null) {
            if (dropping) {
                unit.holding = null;
                reportEffect(obs, unit, getDroppedEffect());
            } else {
                unit.holding = type;
                reportEffect(obs, unit, getPickedUpEffect());
            }
        }

        if (drop != null) {
            addAndReport(bangobj, drop, obs);
        }
        return true;
    }

    @Override // from Effect
    public EffectHandler createHandler (BangObject bangobj)
    {
        return new HoldHandler();
    }

    @Override // from BonusEffect
    public int getBonusPoints (BangObject bangobj)
    {
        return 5; // give 'em five points for picking something up
    }

    @Override // from Effect
    protected String getActivatedEffect ()
    {
        return null;
    }

    /** Contains identifiers for all bonus drop effects.  Subclasses should add
     * their identifiers to the set in a static initializer. */
    protected static HashSet<String> _droppedEffects = new HashSet<String>();
    static {
        _droppedEffects.add(DROPPED_BONUS);
    }
}
