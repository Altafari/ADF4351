package adfctrl.system;

import adfctrl.icmodel.ADF4351Configurator;
import adfctrl.icmodel.ADF4351Proxy;

public class SystemManager {
    
    private static SystemManager instance;
    
    private ADF4351Proxy device;
    private ADF4351Configurator configurator;
    
    private SystemManager() {
        device = new ADF4351Proxy();
        configurator = new ADF4351Configurator(device);
    }

    private static synchronized void createNewInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
    }
    
    public static SystemManager getInstance() {
        if (instance == null) {
            createNewInstance();
        }
        return instance;
    }
    
    public ADF4351Configurator getConfigurator() {
        return configurator;
    }
}
