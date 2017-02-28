package classicslot.app
import java.net.URI
import javax.inject.Inject
import javax.ws.rs.core.Response
import javax.ws.rs.{HeaderParam, _}

import _root_.core.infra.HttpWallet
import _root_.core.model.Money
import classicslot.infra.ClassicSlotRepo

import scala.beans.BeanProperty

case class Spin (@BeanProperty var amount: Money)

@Path("/games/classic-slot")
class ClassicSlotController @Inject() (repo: ClassicSlotRepo) {

  @DELETE
  def delete(@HeaderParam("PlayerId") playerId: String): Response = {
    repo.delete(playerId)
    Response.noContent().build()
  }

  @GET
  def get(@HeaderParam("PlayerId") playerId: String, @HeaderParam("Wallet") uri: URI): Response =
    Response.ok(repo.get(playerId, getWallet(uri))).build()

  @POST
  @Path("/spins")
  def spin(@HeaderParam("PlayerId") playerId: String, @HeaderParam("Wallet") uri: URI, spin: Spin): Response = {
    val wallet = getWallet(uri)
    val slot = repo.get(playerId, wallet).spin(spin.amount)
    Response.created(URI.create(".")).entity(Map(
      "stops" -> slot.stops,
      "balance" -> wallet.getBalance
    )).build()
  }

  private def getWallet(uri: URI) = new HttpWallet(uri, "classic-slot", "classic-slot")
}
