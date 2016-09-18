package cn.com.bright.workflow.exception;

public class ProcessInstanceSuspendException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5208699920655536869L;

    /**
     * Constructs a NoSuchAlgorithmException with no detail message. A detail
     * message is a String that describes this particular exception.
     */
    public ProcessInstanceSuspendException() {
        super();
    }

    /**
     * Constructs a NoSuchAlgorithmException with the specified detail message.
     * A detail message is a String that describes this particular exception,
     * which may, for example, specify which algorithm is not available.
     * @param msg the detail message.
     */
    public ProcessInstanceSuspendException(String msg) {
        super(msg);
    }

    /**
     * Creates a {@code NoSuchAlgorithmException} with the specified detail
     * message and cause.
     * @param message the detail message (which is saved for later retrieval by
     *            the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A {@code null} value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     * @since 1.5
     */
    public ProcessInstanceSuspendException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a {@code NoSuchAlgorithmException} with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())} (which
     * typically contains the class and detail message of {@code cause}).
     * @param cause the cause (which is saved for later retrieval by the
     *            {@link #getCause()} method). (A {@code null} value is
     *            permitted, and indicates that the cause is nonexistent or
     *            unknown.)
     * @since 1.5
     */
    public ProcessInstanceSuspendException(Throwable cause) {
        super(cause);
    }
}
