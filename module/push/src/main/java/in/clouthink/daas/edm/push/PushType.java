package in.clouthink.daas.edm.push;

/**
 * The message type to adapt the jpush lib.
 */
public enum PushType {

	/**
	 * all the targets
	 */
	All,
	/**
	 * Please use tag_or to replace group
	 */
	@Deprecated Group,
	/**
	 * The target with tag ( or relatioinships )
	 */
	TagOr,
	/**
	 * The target with tag ( and relationship )
	 */
	TagAnd,
	/**
	 * The target alias ( different target can with same alias )
	 */
	Alias,
	/**
	 * Please use alias to replace device
	 */
	@Deprecated Device
}
