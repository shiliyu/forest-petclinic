package org.forestframework.samples.petclinic.vet

import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine
import org.forestframework.annotation.Get
import org.forestframework.annotation.TemplateRendering
import org.forestframework.annotation.ThymeleafTemplateRendering
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

class VetRouter @Inject constructor(private val vets: VetRepository) {
    @Get("/vets.html")
    @TemplateRendering(engine = ThymeleafTemplateEngine::class)
    suspend fun showVetList(routingContext: RoutingContext): String {
        ThymeleafTemplateEngine.create(Vertx.vertx());

        routingContext.put("vets", Vets().also { it.vetList.addAll(vets.findAll()) })
        return "vets/vetList"
    }

    @Get("/vets.html")
    @ThymeleafTemplateRendering
    suspend fun showVetList2(routingContext: RoutingContext): String {
        routingContext.put("vets", Vets().also { it.vetList.addAll(vets.findAll()) })
        return "vets/vetList"
    }

    @GET
    @Path("/vets")
    @Produces("applicationi/json")
    suspend fun showResourcesVetList() = Vets().also { it.vetList.addAll(vets.findAll()) }
}