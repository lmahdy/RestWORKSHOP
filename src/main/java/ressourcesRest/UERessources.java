package ressourcesRest;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("UE")
public class UERessources {

    public UniteEnseignementBusiness ueb= new UniteEnseignementBusiness();
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response ajouterUE(UniteEnseignement ue) {
       if (ueb.addUniteEnseignement(ue))
           return Response.status(Response.Status.CREATED).build();

           return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listerUE(@QueryParam("code") Integer code, @QueryParam("semestre") Integer semestre) {
        // Priorité au paramètre 'code' pour retourner un seul objet
        if (code != null) {
            UniteEnseignement ue = ueb.getUEByCode(code);
            if (ue != null) {
                return Response.ok(ue).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Sinon, filtrer par semestre s'il est fourni
        List<UniteEnseignement> result = (semestre == null)
                ? ueb.getListeUE()
                : ueb.getUEBySemestre(semestre);
        return Response.ok(result).build();
    }

    @PUT
    @Path("{code}")
    @Consumes(MediaType.APPLICATION_XML)
    public Response modifierUE(@PathParam("code") int code, UniteEnseignement ue) {
        // Assurer que l'identifiant utilisé est celui du path
        ue.setCode(code);
        boolean ok = ueb.updateUniteEnseignement(code, ue);
        if (ok) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{code}")
    public Response supprimerUE(@PathParam("code") int code) {
        boolean deleted = ueb.deleteUniteEnseignement(code);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
