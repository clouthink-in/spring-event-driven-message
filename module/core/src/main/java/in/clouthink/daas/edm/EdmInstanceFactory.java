package in.clouthink.daas.edm;

/**
 * The class implements this interface should create new instance for each request
 */
public interface EdmInstanceFactory {

	/**
	 *
	 * @return
	 */
	Edm newInstance();

}
