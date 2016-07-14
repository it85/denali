package app;

import com.google.inject.name.Names;
import data.TraderImpl;

public class TraderModule extends DenaliModule {

    public TraderModule() {
        super(TraderModule.class.getSimpleName());
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(Names.named("trader.startingBalance")).to(getConfig().getDouble("trader.startingBalance"));
        bind(Trader.class).to(TraderImpl.class);
    }
}
