package mobrepellent;

public class MobRepellentDelayLoadRunnable implements Runnable
{
    private final MobRepellent plugin;

    public MobRepellentDelayLoadRunnable( MobRepellent plugin )
    {
        this.plugin = plugin;
    }

    @Override
    public void run()
    {
        plugin.loadPlugin( true );
    }
}
