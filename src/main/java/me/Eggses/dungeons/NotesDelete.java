package me.Eggses.dungeons;

public class NotesDelete {

    /*

    Okay a lot of changes - we are sort of going to move back to the OLD event system.
        Dungeon Event Router will hold methods like handleEntityDamageEvent(EntityDmaageEvent)
        along with handleEntityDamageByEntityEvent(...) and handleEpxlosionEvent(entityexplodeevent e)..
    along with handlePlayerRespawn... handle etc

     Listener just fowards its Event into the event router... now either the listener OR the event router
     will pull the world out of the event....S


    and it will be like it was in the past... However -> dungeon instance will be different... rather than
            having methods that handle events like before (like what we have above)... it will provide a getter
    method that returns a InstanceEventHandler object... this class will have a reference to

    Area Controller... EntityManager.... and the DungeonInstance

    now--> this class will do all the logic on events related to dungeons.... so that includes...
    checking like okay if a player left a world that was a dungeon world... add them to the set etc...
    or the interaciton eveent pulling out the block postion if it was the right one etc.... or doing
    the entity dmaage by entity logic that is like 200 lines long...

    Then each of these may call like the eentityManager.handleEvent() yet this can remain the same... its nice as it
    is I think... might need to trace it if the events still do not work but yeah...


    Then... Area Controller will have methods that WILL NOT HANDLE EVENTS. no method in area controller say accept
    and event... this is critical...for things such as the graveyard respawn -> note that area controller
    can provide a getter to the graveyard...
    and in InstanceEVentHandler -> you handle it there...

    for player quit / change world events... the InstanceEVentHandler has a refernce BACK to the dungeon
    isntance and can therefore call public remove/add player methods/....


    So to add a new Event

    1) new listener
    2) new router method
    3) new method in InstnaceEventHandler (nice name distinct makes it known that its related to the instance)
    4) then the above method may handle stuff...


    area controller should have 0 bukkit.event imports... mob death event.,.,. just pass in the UUID of the mob that died

    So clear breakdown of responsilbity:

    1) lsitener listeners to event
    2) router finds the world the event took place in... and if it exists... it gets the DungeonsInstance of
    that world... and then gets the InstanceEventHandler from the DungeonInsdtance... the event is passed there
    it then does domain logic... eg working out player enter / left... or working out movememnt... or working out
    X or Y or whatever... or working out who died and where... etc....

    then it may

    pass some details NOT the event to the DungeonInstance (eg to add/remove players)
    or
    pass some details NOT the event to the AreaController(eg the destination of where a player moved... or the UUID
    of the mob that died...
    or maybe it gets detials such as the graveyard object...(which area controller may not even need.)
    or it can who knows...

and then... for each of these cases in the InstanceEventRouter... it can pull the entity... and check like
hey is this a dungeon entity... if so get its eventHandler object... and pass into the generic
event method which porbably works fine i am guessing...

note 1000% sure becuase of the creeper explosion stuff but who knows....


IT IS CRITICAL THAT AREA CONTORLLER DOES NOT ACCEPT ANY EVENT... AND ALL EVENTS SHOULD GO THROUGH THE
INSTANCE EVENT HANDLER... BOTH MOVEMENT IN AND OUT OF DUNGEONS TOO... ITS LIKE 4 EXTRA CALLQ AND RETQ ASSEMBLY CALLS
TOTAL WHICH IS LIKE IRRELEIVENT....

NO EVENT HANDLING SHOULD BE DONE OUTSIDE... LIKE THE WORLD CHANGE STUFF... THAT SHOULD BE ALL
HANDLED INTERNALLY IN INSTANCE EVENT ROUTE.... OR THE INTERACITON SUFF... SAME WITH THE ENTITY DMADGE ENETIY EVENT T
THAT SHOULD GET PASSED FURTHER IN AS WELL...


*** ALso as dungeon entity now has a single handle Event method (as that generic is poorbably good)
probably easier to jsut have a handleEvent method as oopposed to getting the event behaviour as currently
done... makes it a lot wasier to call stuff... porbably do this..


dungeon flaming projecitles needs to be its own plugin....

to add item events... the event listener can get a

@EventHandler
public void X(X event) {
dungeonEventRouter.handleEventX(event)
itemEventRouter.handleEventX(event)
}



// TODO READ THE ABOVE
// TODO READ THE ABOVE
// TODO READ THE ABOVE
// TODO READ THE ABOVE
// TODO READ THE ABOVE












     */




    /*
    playing disconeecting causes the dungeon to throw a fail to delete error... maybe in this case just
    like do the non file delete?
     */




    /*
    item ban used to do:
                event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);

     see if tridents are still blocked along with pots...
     */




        /*
    isntead of flaming skeeltons
            put code here that ignites all
            arrows when
                    shot.
                            if in the dungoen world or actually no in all worlds
            okay becuase its in all edit flaming skeeltons but relaistically make a new plugin
    idk jus tleabe here for now then when you make your SMP plugin put all of that logic in there...


     */

}
