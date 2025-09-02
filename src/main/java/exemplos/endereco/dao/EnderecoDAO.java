/*
 * Universidade Estadual de Maringá - UEM
 * Núcleo de Processamento de Dados - NPD
 * Copyright (c) 2020. All rights reserved.
 */
package exemplos.endereco.dao;

import dao.DAO;
import exemplos.endereco.model.EnderecoDTO;
import java.util.List;
import javax.persistence.Query;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;
import utilitarios.Utils;

/**
 *
 * @author alison
 */
public class EnderecoDAO extends DAO {

    private final Utils utils = new Utils();

    public EnderecoDTO buscarEnderecoPorCep(String cep) {

        Session sessao = openSession();

        try {

            Query consulta = sessao.createNativeQuery(""
                    + "SELECT "
                    + "	UFE_SG AS SG_UF, "
                    + "	LOC_NU AS NU_MUNICIPIO, "
                    + "	LOC_NO AS NM_MUNICIPIO, "
                    + "	BAI_NO AS NM_BAIRRO, "
                    + "	LOG_NO AS NM_LOGRADOURO, "
                    + "	CASE WHEN LENGTH(TRANSLATE(MUN_NU,'',TRANSLATE(MUN_NU,'','1234567890',''),'')) > 0 THEN INTEGER(TRANSLATE(MUN_NU,'',TRANSLATE(MUN_NU,'','1234567890',''),'')) ELSE NULL END AS CD_IBGE_MUNICIPIO, "
                    + "	CASE WHEN LENGTH(TRANSLATE(CEP,'',TRANSLATE(CEP,'','1234567890',''),'')) > 0 THEN INTEGER(TRANSLATE(CEP,'',TRANSLATE(CEP,'','1234567890',''),'')) ELSE NULL END AS NU_CEP "
                    + "FROM "
                    + "	( "
                    + "         SELECT "
                    + "			T1.UFE_SG, "
                    + "			T2.LOC_NO, "
                    + "			T2.LOC_NU, "
                    + "			T3.BAI_NO, "
                    + "   		T3.BAI_NO_ABREV, "
                    + "			T1.TLO_TX || ' ' || T1.LOG_NO AS LOG_NO, "
                    + "			T1.LOG_NO_ABREV AS LOG_NO_ABREV, "
                    + "			T1.CEP, "
                    + "			T2.MUN_NU, "
                    + "                 T2.LOC_IN_TIPO_LOC, "
                    + "                 T2.LOC_NU_SUB "
                    + "		FROM "
                    + "			DNE.LOG_LOGRADOURO T1, "
                    + "			DNE.LOG_LOCALIDADE T2, "
                    + "			DNE.LOG_BAIRRO T3 "
                    + "		WHERE "
                    + "			T1.LOC_NU = T2.LOC_NU "
                    + "			AND T1.BAI_NU_INI = T3.BAI_NU "
                    + "			AND T1.LOG_STA_TLO = 'S' "
                    + "		UNION "
                    + "		SELECT "
                    + "			T1.UFE_SG, "
                    + "			T2.LOC_NO, "
                    + "			T2.LOC_NU, "
                    + "			T3.BAI_NO, "
                    + "			T3.BAI_NO_ABREV, "
                    + "			T1.LOG_NO AS LOG_NO, "
                    + "			T1.LOG_NO_ABREV AS LOG_NO_ABREV, "
                    + "			T1.CEP, "
                    + "			T2.MUN_NU, "
                    + "                 T2.LOC_IN_TIPO_LOC, "
                    + "                 T2.LOC_NU_SUB "
                    + "		FROM "
                    + "			DNE.LOG_LOGRADOURO T1, "
                    + "			DNE.LOG_LOCALIDADE T2, "
                    + "			DNE.LOG_BAIRRO T3 "
                    + "		WHERE "
                    + "			T1.LOC_NU = T2.LOC_NU "
                    + "			AND T1.BAI_NU_INI = T3.BAI_NU "
                    + "			AND T1.LOG_STA_TLO = 'N' "
                    + "		UNION "
                    + "		SELECT "
                    + "			T2.UFE_SG, "
                    + "			T2.LOC_NO, "
                    + "			T2.LOC_NU, "
                    + "			'CENTRO' AS BAI_NO, "
                    + "			'CENTRO' AS BAI_NO_ABREV, "
                    + "			'PRINCIPAL' AS LOG_NO, "
                    + "			'PRINCIPAL' AS LOG_NO_ABREV, "
                    + "			T2.CEP, "
                    + "			T2.MUN_NU, "
                    + "                 T2.LOC_IN_TIPO_LOC, "
                    + "                 T2.LOC_NU_SUB "
                    + "		FROM "
                    + "			DNE.LOG_LOCALIDADE T2 "
                    + "		WHERE "
                    + "			T2.CEP IS NOT NULL "
                    + "		UNION "
                    + "		SELECT "
                    + "			T4.UFE_SG, "
                    + "			T2.LOC_NO, "
                    + "			T2.LOC_NU, "
                    + "			'CENTRO' AS BAI_NO, "
                    + "			'CENTRO' AS BAI_NO_ABREV, "
                    + "			T4.CPC_ENDERECO AS LOG_NO, "
                    + "			T4.CPC_ENDERECO AS LOG_NO_ABREV, "
                    + "			T4.CEP, "
                    + "			T2.MUN_NU, "
                    + "                 T2.LOC_IN_TIPO_LOC, "
                    + "                 T2.LOC_NU_SUB "
                    + "		FROM "
                    + "			DNE.LOG_CPC T4, "
                    + "			DNE.LOG_LOCALIDADE T2 "
                    + "		WHERE "
                    + "			T4.LOC_NU = T2.LOC_NU "
                    + "		UNION "
                    + "		SELECT "
                    + "			T5.UFE_SG, "
                    + "			T2.LOC_NO, "
                    + "			T2.LOC_NU, "
                    + "			T3.BAI_NO AS BAI_NO, "
                    + "			T3.BAI_NO_ABREV AS BAI_NO_ABREV, "
                    + "			T5.GRU_ENDERECO AS LOG_NO, "
                    + "			T5.GRU_ENDERECO AS LOG_NO_ABREV, "
                    + "			T5.CEP, "
                    + "			T2.MUN_NU, "
                    + "                 T2.LOC_IN_TIPO_LOC, "
                    + "                 T2.LOC_NU_SUB "
                    + "		FROM "
                    + "			DNE.LOG_GRANDE_USUARIO T5, "
                    + "			DNE.LOG_LOCALIDADE T2, "
                    + "			DNE.LOG_BAIRRO T3 "
                    + "		WHERE "
                    + "			T5.LOC_NU = T2.LOC_NU "
                    + "			AND T5.BAI_NU = T3.BAI_NU "
                    + "		UNION "
                    + "		SELECT "
                    + "			T6.UFE_SG, "
                    + "			T2.LOC_NO, "
                    + "			T2.LOC_NU, "
                    + "			T3.BAI_NO AS BAI_NO, "
                    + "			T3.BAI_NO_ABREV AS BAI_NO_ABREV, "
                    + "			T6.UOP_ENDERECO AS LOG_NO, "
                    + "			T6.UOP_ENDERECO AS LOG_NO_ABREV, "
                    + "			T6.CEP, "
                    + "			T2.MUN_NU, "
                    + "                 T2.LOC_IN_TIPO_LOC, "
                    + "                 T2.LOC_NU_SUB "
                    + "		FROM "
                    + "			DNE.LOG_UNID_OPER T6, "
                    + "			DNE.LOG_LOCALIDADE T2, "
                    + "			DNE.LOG_BAIRRO T3 "
                    + "		WHERE "
                    + "			T6.LOC_NU = T2.LOC_NU "
                    + "			AND T6.BAI_NU = T3.BAI_NU "
                    + "	) "
                    + "WHERE "
                    + "	CEP = :cep ");

            consulta.setParameter("cep", utils.soNumeros(cep));

            List<Object> objetos = consulta.getResultList();

            if (objetos != null && !objetos.isEmpty()) {

                Object[] objCampos = (Object[]) objetos.get(0);

                EnderecoDTO endereco = new EnderecoDTO();

                endereco.setSgUf((String) objCampos[0]);
                endereco.setNuMunicipio((Integer) objCampos[1]);
                endereco.setNmMunicipio((String) objCampos[2]);
                endereco.setNmBairro((String) objCampos[3]);
                endereco.setNmLogradouro((String) objCampos[4]);
                endereco.setCdIbgeMunicipio((Integer) objCampos[5]);
                endereco.setNuCep((Integer) objCampos[6]);

                return endereco;
            }

            return null;

        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
            return null;
        } finally {
            closeSession(sessao);
        }
    }
}
