package ressourcesRest;

import entities.Module;
import metiers.ModuleBusiness;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("modules")
public class ModuleRessources {

    private final ModuleBusiness moduleBusiness = new ModuleBusiness();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ajouterModule(Module module) {
        boolean ok = moduleBusiness.addModule(module);
        if (ok) {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listerModules() {
        List<Module> list = moduleBusiness.getAllModules();
        return Response.ok(list).build();
    }

    @GET
    @Path("{matricule}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModule(@PathParam("matricule") String matricule) {
        Module m = moduleBusiness.getByMatricule(matricule);
        if (m != null) {
            return Response.ok(m).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{matricule}")
    public Response supprimerModule(@PathParam("matricule") String matricule) {
        boolean deleted = moduleBusiness.deleteModule(matricule);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{matricule}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifierModule(@PathParam("matricule") String matricule, Module module) {
        // enforce path matricule
        module.setMatricule(matricule);
        boolean ok = moduleBusiness.updateModule(matricule, module);
        if (ok) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("UE")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listerModulesParUE(@QueryParam("codeUE") Integer codeUE) {
        if (codeUE == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("codeUE query param is required").build();
        }
        List<Module> list = moduleBusiness.getByUECode(codeUE);
        if (list == null || list.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(list).build();
    }
}
