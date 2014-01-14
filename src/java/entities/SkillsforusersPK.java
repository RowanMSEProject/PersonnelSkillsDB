/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author crouch
 */
@Embeddable
public class SkillsforusersPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "USERID")
    private String userid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SKILLSID")
    private int skillsid;

    public SkillsforusersPK() {
    }

    public SkillsforusersPK(String userid, int skillsid) {
        this.userid = userid;
        this.skillsid = skillsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getSkillsid() {
        return skillsid;
    }

    public void setSkillsid(int skillsid) {
        this.skillsid = skillsid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        hash += (int) skillsid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SkillsforusersPK)) {
            return false;
        }
        SkillsforusersPK other = (SkillsforusersPK) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        if (this.skillsid != other.skillsid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.SkillsforusersPK[ userid=" + userid + ", skillsid=" + skillsid + " ]";
    }
    
}
