package de.flower.rmt.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author oblume
 */
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

    /** This constant is only needed for serialization purposes. It overwrites the default mechanism and so makes the BE longer binary compatible */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     /**
     * Gets the id.
     *
     * @return the id
     */
    public  Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // public abstract void setId(Long id);

	/**
	 * Returns true if this entity is transient (not saved yet).
	 *
	 * @return true, if checks if is transient
	 */
	public boolean isTransient() {
		return (getId() == null);
	}

	//----Constructors----

	//---Methods
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder retVal = new StringBuilder();

		retVal.append("[AbstractBaseEntity]");

		return retVal.toString();
	}

	/**
	 * Copies only the state, not the references - so the copy is shallow.
	 * The @Id fields are not copied. Reason: you cannot copy a persistent object
	 * with already existing primary key.
	 * Subclasses must override this method and copy field for field to
	 * the new object.
	 *
	 * @return a shallow copy of the BaseBE
	 */
	protected AbstractBaseEntity copyShallow() {
		AbstractBaseEntity shallowCopy = null;

		try {
			shallowCopy = this.getClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Cannot shallow copy class: "
					+ this.getClass() + " due to reflection problems.", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Cannot shallow copy class: "
					+ this.getClass() + " due to reflection problems.", e);
		}

		return shallowCopy;
	}

	/**
	 * Update from object.
	 *
	 * @param other the other
	 */
	public void updateFromObject(AbstractBaseEntity other) {
		throw new UnsupportedOperationException("Method must be implemented in subclass!");
	}

    /** The equality object. */
     @Transient
     private Object equalityObject;

	/**
	 * The following methods are taken from
	 * http://forum.hibernate.org/viewtopic.php?p=2191778.
	 *
	 * @param other the other
	 *
	 * @return true, if equals
	 */
	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}

		if ((other == null)) {
			return false;
		}

		if (!(this.getClass().isInstance(other))) {
			return false;
		}

		AbstractBaseEntity castOther = (AbstractBaseEntity) other;

		if (this.getId() == null) {
			return false;
		}

		if (castOther.getId() == null) {
			return false;
		}

		boolean equals = ((this.getId() == castOther.getId()) || ((this.getId() != null)
				&& (castOther.getId() != null) && this.getId().equals(
				castOther.getId())));

		return equals;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		Object o = getEqualityObject();

		if (o == this) {
			return super.hashCode();
		} else {
			return o.hashCode();
		}
	}

	/**
	 * Gets the equality object.
	 *
	 * @return the equality object
	 */
	private Object getEqualityObject() {
		if (equalityObject == null) {
			equalityObject = (getId() == null) ? this : getId();
		}

		return equalityObject;
	}

}
