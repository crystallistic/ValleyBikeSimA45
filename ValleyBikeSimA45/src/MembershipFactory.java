/**
 * @author maingo
 *
 */
public class MembershipFactory {

	public MembershipFactory() {
	};

	public Membership getMembership(String membershipType) {

		if (membershipType == null) {
			return null;
		}

		if (membershipType.equalsIgnoreCase("PayPerRide")) {
			return new PayPerRide();
		} else if (membershipType.equalsIgnoreCase("DayPass")) {
			return new DayPass();
		} else if (membershipType.equalsIgnoreCase("Monthly")) {
			return new Monthly();
		} else if (membershipType.equalsIgnoreCase("Yearly")) {
			return new Yearly();
		} else if (membershipType.equalsIgnoreCase("FoundingMember")) {
			return new FoundingMember();
		}
		return null;

	}
}
