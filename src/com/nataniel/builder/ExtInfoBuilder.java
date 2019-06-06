/*
 * Copyright (C) 2019 Nataniel
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package com.nataniel.builder;

import com.nataniel.inter.ExtInfo;

import java.security.SecureRandom;
import java.util.HashSet;

/**
 * @author Neoold
 */
public class ExtInfoBuilder {
    private static SecureRandom random;
    private static HashSet<Integer> ids;
    //dados do ext
    private String LogoURL = null;
    private String Id = null;
    private String Grupo = null;
    private String CanalNome = null;
    private String CanalURL = null;
    private int unid;

    public ExtInfoBuilder() {
        if (random == null) {
            random = new SecureRandom();
        }
        if (ids == null) {
            ids = new HashSet<>();
        }
        int i = random.nextInt();
        while (ids.contains(i)) {
            i = random.nextInt();
        }
    }

    public void setLogoURL(String LogoURL) {
        this.LogoURL = LogoURL;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setGrupo(String Grupo) {
        this.Grupo = Grupo;
    }

    public void setCanalNome(String CanalNome) {
        this.CanalNome = CanalNome;
    }

    public void setCanalURL(String CanalURL) {
        this.CanalURL = CanalURL;
    }

    public ExtInfo builder() {
        return new ExtInfo() {
            @Override
            public String getLogoURL() {
                return LogoURL;
            }

            @Override
            public String getId() {
                return Id;
            }

            @Override
            public String getGrupo() {
                return Grupo;
            }

            @Override
            public String getCanalNome() {
                return CanalNome;
            }

            @Override
            public String getCanalURL() {
                return CanalURL;
            }

            @Override
            public boolean equals(Object o) {
                return (o instanceof ExtInfo) && o.hashCode() == this.hashCode();
            }

            @Override
            public int hashCode() {
                return unid;
            }

            @Override
            public String toString() {
                return getCanalNome();
            }
        };
    }

}
