package exemplos.endereco.controller;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import exemplos.endereco.dao.EnderecoDAO;
import exemplos.endereco.dao.EstadoDAO;
import exemplos.endereco.dao.MunicipioDAO;
import exemplos.endereco.model.EnderecoDTO;
import exemplos.endereco.model.Estado;
import exemplos.endereco.model.Municipio;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listitem;
import utilitarios.Utils;
import utilitarios.ZkUtils;
import zk.custom.Toast;

/**
 *
 * @author alison
 */
public class EnderecoController extends Window {

    private Window win;

    private Textbox cep;
    private Textbox logradouro;
    private Textbox numero;
    private Textbox bairro;
    private Listbox municipio;
    private Listbox uf;

    private final Utils utils = new Utils();
    private final ZkUtils zkUtils = new ZkUtils();

    private final EstadoDAO estadoDao = new EstadoDAO();
    private final MunicipioDAO municipioDao = new MunicipioDAO();

    public void onCreate() {
        this.win = (Window) getFellow("winEndereco");

        this.cep = (Textbox) getFellow("cep");
        this.logradouro = (Textbox) getFellow("logradouro");
        this.numero = (Textbox) getFellow("numero");
        this.bairro = (Textbox) getFellow("bairro");
        this.municipio = (Listbox) getFellow("municipio");
        this.uf = (Listbox) getFellow("uf");

        this.carregaEstados();
    }

    private void carregaEstados() {
        for (Estado estado : estadoDao.listar()) {
            Listitem li = new Listitem();
            Listitem li2 = new Listitem();
            li.setValue(estado.getSgUf());
            li.setLabel(estado.getSgUf());
            li2.setValue(estado.getSgUf());
            li2.setLabel(estado.getSgUf());
            li.setParent(this.uf);
        }
    }

    public void populaCidades() {
        if (this.uf.getSelectedItem() != null && !this.uf.getSelectedItem().getValue().equals("")) {
            String siglaEstadoSelecionado = this.uf.getSelectedItem().getValue();
            this.municipio.clearSelection();
            this.municipio.getItems().clear();
            for (Municipio mun : municipioDao.listarByEstado(siglaEstadoSelecionado)) {
                Listitem li = new Listitem();
                li.setValue(mun.getNuMunicipio());
                li.setLabel(mun.getNmMunicipio());
                li.setAttribute("objeto", mun);
                li.setParent(this.municipio);
            }
        }
    }

    public void populaEnderecoPorCep() {
        String cepSoNumeros = utils.soNumeros(this.cep.getValue());
        if (cepSoNumeros.length() == 8) {
            EnderecoDAO ad = new EnderecoDAO();
            EnderecoDTO endereco = ad.buscarEnderecoPorCep(cepSoNumeros);
            if (endereco != null) {
                if (endereco.getNmLogradouro().length() > 0) {
                    this.logradouro.setValue(endereco.getNmLogradouro());
                }

                if (endereco.getNmBairro().length() > 0) {
                    this.bairro.setValue(endereco.getNmBairro());
                }
                zkUtils.selecionarListaLabelSiglaUF(this.uf, endereco.getSgUf());
                populaCidades();
                zkUtils.selecionarLista(this.municipio, endereco.getNuMunicipio());
            } else {
                Clients.showNotification("O CEP não foi encontrado! Verifique se está correto.", Clients.NOTIFICATION_TYPE_ERROR, this.cep, "end_center", 0);
            }
        }
    }

    public boolean validarCampos() {
        boolean gerouErro = false;

        if (this.cep.getValue().isBlank()) {
            Clients.showNotification("CEP é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.cep, "end_center", 0);
            gerouErro = true;
        } else {
            if (utils.soNumeros(this.cep.getValue()).length() < 8) {
                Clients.showNotification("CEP é inválido!", Clients.NOTIFICATION_TYPE_ERROR, this.cep, "end_center", 0);
                gerouErro = true;
            }
        }
        if (this.uf.getSelectedItem() == null) {
            Clients.showNotification("UF é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.uf, "end_center", 0);
            gerouErro = true;
        }
        if (this.municipio.getSelectedItem() == null) {
            Clients.showNotification("Município é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.municipio, "end_center", 0);
            gerouErro = true;
        }
        if (this.bairro.getValue().trim().equals("")) {
            Clients.showNotification("Bairro é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.bairro, "end_center", 0);
            gerouErro = true;
        }
        if (this.logradouro.getValue().trim().equals("")) {
            Clients.showNotification("Logradouro é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.logradouro, "end_center", 0);
            gerouErro = true;
        }
        if (this.numero.getValue().trim().equals("")) {
            Clients.showNotification("Número é obrigatório!", Clients.NOTIFICATION_TYPE_WARNING, this.numero, "end_center", 0);
            gerouErro = true;
        }
        return !gerouErro;
    }

    public void salvar() {
        if (this.validarCampos()) {
            Toast.show("Endereço validado com sucesso!", "Sucesso", Toast.Type.SUCCESS);
        }
    }

}
