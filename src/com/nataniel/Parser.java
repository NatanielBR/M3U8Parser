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

package com.nataniel;

import com.nataniel.builder.ExtInfoBuilder;
import com.nataniel.inter.ExtInfo;
import com.nataniel.list.ExtInfoList;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * @author Neoold
 */
public class Parser {
    private static final String[] ATTRS = new String[]{"tvg-logo", "tvg-id", "group-title"};

    /**
     * Metodo para fazer o parser do ExtM3U8 para o java. Suporte somente ao M3U8.
     * Caso nenhum dos atributos existe no original, o parser irá atribuir uma String
     * vazia, isso foi feito para evitar Exceptions de NullPoint na aplicação original.
     *
     * @param dados InputStream do arquivo ou da url.
     * @return Retorna um ExtInfoList, podendo ter ou não conteudo. Utilize o metodo getExtInfoCount para
     * saber se esta vazia.
     * @see ExtInfoList
     */
    public static ExtInfoList parserExtM3u8(InputStream dados) {
        Scanner ent = new Scanner(dados);
        ExtInfoBuilder bu = new ExtInfoBuilder();
        HashMap<String, String> attrValue = new HashMap<>();
        List<ExtInfo> info = new ArrayList<>();
        while (ent.hasNextLine()) {
            String linha = ent.nextLine();
            if (linha.startsWith("#EXTINF")) {
                StringBuilder builder = new StringBuilder();
                String chave = null;
                boolean start = false;
                int ini = StringUtils.indexOfAny(linha, ATTRS);
                if (ini != -1) {
                    String nlinha = linha.substring(ini);
                    for (char c : nlinha.toCharArray()) {
                        builder.append(c);
                        if (!start && StringUtils.containsAny(builder.toString(), ATTRS)) {
                            chave = builder.toString();
                            builder.setLength(0);
                        }
                        if (chave != null && c == '"') {
                            if (start) {
                                if (StringUtils.containsAny(builder.toString(), ATTRS)) {
                                    // Correcao para um bug quando um dos atribuos como o exemplo:
                                    // tvg-id=" group-title="XXX"
                                    attrValue.put(chave.trim(), "");
                                    chave = builder.toString().trim().replace("=\"", "");
                                    start = !start;
                                } else {
                                    attrValue.put(chave.trim(), builder.toString().replace("\"", "").trim());
                                    chave = null;
                                }
                            }
                            start = !start;
                            builder.setLength(0);

                        }
                    }
                    bu.setGrupo(attrValue.getOrDefault(ATTRS[2], ""));
                    bu.setId(attrValue.getOrDefault(ATTRS[1], ""));
                    bu.setLogoURL(attrValue.getOrDefault(ATTRS[0], ""));
                    bu.setCanalNome(StringUtils.substringAfterLast(nlinha, "\",").trim());
                } else {
                    String nome = StringUtils.substringAfterLast(linha, ",").trim();
                    if (nome.contains(":")) {
                        String[] lis = nome.split(":");
                        bu.setCanalNome(lis[1]);
                        bu.setGrupo(lis[0]);
                    } else {
                        bu.setCanalNome(nome);
                    }
                }

            } else if (linha.startsWith("http")) {
                bu.setCanalURL(linha);
                info.add(bu.builder());
                bu = new ExtInfoBuilder();
                attrValue.clear();
            }
        }
        return new ExtInfoList(info);
    }

    /**
     * Metodo para criar um arquivo ExtM3U usando o ExtInfoList com fonte de dados
     * e o File como o arquivo. Essa forma de criar o arquivo irá colocar alguns
     * atributos, uteis para o projeto original porém (inutil?) para outros
     * programas.
     *
     * @param channelList O ExtInfoList contendo os dados para ser serializado.
     * @param file        O arquivo onde vai ser escrito os dados no formato ExtM3U.
     * @throws FileNotFoundException Caso o arquivo nao exista.
     */
    public static void ParserChannelListToFile(ExtInfoList channelList, File file) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(file);
        printStream.println("#EXTM3U");

        for (ExtInfo info : channelList.getAllExtInfo()) {
            String linha = "#EXTINF:-1";
            if (!info.getLogoURL().isEmpty()) {
                linha = linha.concat(String.format(" %s=\"%s\"", ATTRS[0], info.getLogoURL()));
            }
            if (!info.getGrupo().isEmpty()) {
                linha = linha.concat(String.format(" %s=\"%s\"", ATTRS[2], info.getGrupo()));
            }
            if (!info.getId().isEmpty()) {
                linha = linha.concat(String.format(" %s=\"%s\"", ATTRS[1], info.getId()));
            }
            linha = linha.concat(String.format(",%s", info.getCanalNome()));
            printStream.println(linha);
            printStream.println(info.getCanalURL());
        }
    }

    private static int stringFirts(String f) {
        int foc = -1;
        for (String at : ATTRS) {
            int atfoc = f.indexOf(at);
            if (atfoc > foc) {
                foc = atfoc;
                break;
            }
        }
        return foc;
    }

    private static boolean stringCont(String string) {
        boolean ret = false;
        while (string.startsWith(" ")) {
            string = string.substring(1);
        }
        while (string.endsWith(" ")) {
            string = string.substring(0, string.length() - 1);
        }
        for (String s : ATTRS) {
            if (s.equals(string)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
