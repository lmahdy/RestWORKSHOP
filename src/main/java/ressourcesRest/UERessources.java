package ressourcesRest;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("UE")
public class UERessources {

    public UniteEnseignementBusiness ueb= new UniteEnseignementBusiness();
    @POST
    //@Consumes("application/xml")
    @Consumes(MediaType.APPLICATION_XML)
    public Response ajouterUE(UniteEnseignement ue) {
       if (ueb.addUniteEnseignement(ue))
           return Response.status(Response.Status.CREATED).build();

            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}
