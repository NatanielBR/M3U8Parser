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

package com.nataniel.list;

import com.nataniel.inter.ExtInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExtInfoList {
    private List<ExtInfo> list;
    private Set<String> groups;

    /**
     * Construtor utilizado pelo Parser para criar o objeto.
     *
     * @param list
     */
    public ExtInfoList(List<ExtInfo> list) {
        this.list = list;
        groups = new HashSet<>();
        groups.addAll(list.stream().map(a -> a.getGrupo()).collect(Collectors.toList()));
    }

    /**
     * Metodo para obter uma lsita, sem repetiçoes, de grupos da lista original.
     *
     * @return uma lista sem repetiçoes podendo estar vazia.
     */
    public Set<String> getAllGroups() {
        return groups;
    }

    /**
     * Metodo para obter uma lista de ExtInfo baseado no Id.
     * Caso o EXTINFO original não tenha Id (tvg-id) o Parser
     * irá atribuir o valor para 0. Então esse metodo poderá não
     * poderá ser util caso o criador não tenha indicado o Id
     * ou caso o Id seja todos os mesmos.
     *
     * @param id Id de um ou mais EXTINFO.
     * @return Uma lista de EXTIINFO podendo esta vazia ou não.
     */
    public List<ExtInfo> getExtInfoByGroupId(String id) {
        return list.stream().filter(a -> a.getId().equals(id)).collect(Collectors.toList());
    }

    /**
     * Metodo para obter uma lista de EXTINFO baseado no nome do
     * Grupo informado no EXTM3U8 original.
     *
     * @param name Nome do grupo para a pesquisa
     * @return Uma lista onde o conteudo é uma lista de EXTINFO do mesmo grupo.
     */
    public List<ExtInfo> getAllExtInfoByGroupName(String name) {
        return list.stream().filter(a -> a.getGrupo().equals(name)).collect(Collectors.toList());
    }

    /**
     * Metodo para obter uma lista de Nome de cada canal.
     *
     * @return Uma lista de nomes.
     */
    public List<String> getAllExtInfoNames() {
        return list.stream().map(a -> a.getCanalNome()).collect(Collectors.toList());
    }

    public List<ExtInfo> getAllExtInfo() {
        return list;
    }

    /**
     * Metodo para obter a contagem de EXTINFO.
     * Utilize para saber se a lista esta vazia.
     *
     * @return Um numero correspondende ao retorno de List.size() do original.
     */
    public int getExtInfoCount() {
        return list.size();
    }

}
