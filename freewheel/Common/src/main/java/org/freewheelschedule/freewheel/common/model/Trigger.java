package org.freewheelschedule.freewheel.common.model;

import org.hamcrest.TypeSafeMatcher;

import javax.persistence.*;

@Entity
@Table(name = "TRIGGER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TriggerType", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BASIC")
public abstract class Trigger<T> extends TypeSafeMatcher<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
