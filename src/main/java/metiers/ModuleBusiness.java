package metiers;

import entities.Module;
import entities.UniteEnseignement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModuleBusiness {
    private static List<Module> modules;
    private final UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    public ModuleBusiness() {
        if (modules == null) {
            modules = new ArrayList<>();
        }
    }

    public boolean addModule(Module module) {
        // Validate UE existence by code
        UniteEnseignement ue = module.getUniteEnseignement();
        if (ue == null) return false;
        UniteEnseignement existing = ueBusiness.getUEByCode(ue.getCode());
        if (existing == null) return false;
        // attach managed UE instance (to keep consistent reference)
        module.setUniteEnseignement(existing);
        return modules.add(module);
    }

    public List<Module> getAllModules() {
        return modules;
    }

    public Module getByMatricule(String matricule) {
        for (Module m : modules) {
            if (m.getMatricule().equalsIgnoreCase(matricule)) return m;
        }
        return null;
    }

    public boolean deleteModule(String matricule) {
        Iterator<Module> it = modules.iterator();
        while (it.hasNext()) {
            Module m = it.next();
            if (m.getMatricule().equalsIgnoreCase(matricule)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean updateModule(String matricule, Module updated) {
        for (int i = 0; i < modules.size(); i++) {
            if (modules.get(i).getMatricule().equalsIgnoreCase(matricule)) {
                // validate UE
                UniteEnseignement ue = updated.getUniteEnseignement();
                if (ue == null) return false;
                UniteEnseignement existing = ueBusiness.getUEByCode(ue.getCode());
                if (existing == null) return false;
                updated.setUniteEnseignement(existing);
                updated.setMatricule(matricule);
                modules.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public List<Module> getByUECode(int codeUE) {
        List<Module> res = new ArrayList<>();
        for (Module m : modules) {
            if (m.getUniteEnseignement() != null && m.getUniteEnseignement().getCode() == codeUE) {
                res.add(m);
            }
        }
        return res;
    }
}
