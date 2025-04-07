package com.GenCin.GenCin.Aula.DTO;

import java.sql.Time;
import java.util.UUID;

public class AulaDTO {
    private UUID id;
    private String codAula;
    private String nomeAula;

    // Nome e email do professor, apenas o que desejamos mostrar
    private String nomeProfessor;
    private String emailProfessor;

    // Se quiser expor os campos de dia/horário, inclua:
    private boolean seg;
    private Time horaInicioSeg;
    private Time horaFimSeg;

    private boolean ter;
    private Time horaInicioTer;
    private Time horaFimTer;

    private boolean qua;
    private Time horaInicioQua;
    private Time horaFimQua;

    private boolean qui;
    private Time horaInicioQui;
    private Time horaFimQui;

    private boolean sex;
    private Time horaInicioSex;
    private Time horaFimSex;

    private boolean sab;
    private Time horaInicioSab;
    private Time horaFimSab;

    // Getters e setters:
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodAula() {
        return codAula;
    }
    public void setCodAula(String codAula) {
        this.codAula = codAula;
    }

    public String getNomeAula() {
        return nomeAula;
    }
    public void setNomeAula(String nomeAula) {
        this.nomeAula = nomeAula;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }
    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getEmailProfessor() {
        return emailProfessor;
    }
    public void setEmailProfessor(String emailProfessor) {
        this.emailProfessor = emailProfessor;
    }

    public boolean isSeg() {
        return seg;
    }
    public void setSeg(boolean seg) {
        this.seg = seg;
    }

    public Time getHoraInicioSeg() {
        return horaInicioSeg;
    }
    public void setHoraInicioSeg(Time horaInicioSeg) {
        this.horaInicioSeg = horaInicioSeg;
    }

    public Time getHoraFimSeg() {
        return horaFimSeg;
    }
    public void setHoraFimSeg(Time horaFimSeg) {
        this.horaFimSeg = horaFimSeg;
    }

    public boolean isTer() {
        return ter;
    }
    public void setTer(boolean ter) {
        this.ter = ter;
    }

    public Time getHoraInicioTer() {
        return horaInicioTer;
    }
    public void setHoraInicioTer(Time horaInicioTer) {
        this.horaInicioTer = horaInicioTer;
    }

    public Time getHoraFimTer() {
        return horaFimTer;
    }
    public void setHoraFimTer(Time horaFimTer) {
        this.horaFimTer = horaFimTer;
    }

    public boolean isQua() {
        return qua;
    }
    public void setQua(boolean qua) {
        this.qua = qua;
    }

    public Time getHoraInicioQua() {
        return horaInicioQua;
    }
    public void setHoraInicioQua(Time horaInicioQua) {
        this.horaInicioQua = horaInicioQua;
    }

    public Time getHoraFimQua() {
        return horaFimQua;
    }
    public void setHoraFimQua(Time horaFimQua) {
        this.horaFimQua = horaFimQua;
    }

    public boolean isQui() {
        return qui;
    }
    public void setQui(boolean qui) {
        this.qui = qui;
    }

    public Time getHoraInicioQui() {
        return horaInicioQui;
    }
    public void setHoraInicioQui(Time horaInicioQui) {
        this.horaInicioQui = horaInicioQui;
    }

    public Time getHoraFimQui() {
        return horaFimQui;
    }
    public void setHoraFimQui(Time horaFimQui) {
        this.horaFimQui = horaFimQui;
    }

    public boolean isSex() {
        return sex;
    }
    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Time getHoraInicioSex() {
        return horaInicioSex;
    }
    public void setHoraInicioSex(Time horaInicioSex) {
        this.horaInicioSex = horaInicioSex;
    }

    public Time getHoraFimSex() {
        return horaFimSex;
    }
    public void setHoraFimSex(Time horaFimSex) {
        this.horaFimSex = horaFimSex;
    }

    public boolean isSab() {
        return sab;
    }
    public void setSab(boolean sab) {
        this.sab = sab;
    }

    public Time getHoraInicioSab() {
        return horaInicioSab;
    }
    public void setHoraInicioSab(Time horaInicioSab) {
        this.horaInicioSab = horaInicioSab;
    }

    public Time getHoraFimSab() {
        return horaFimSab;
    }
    public void setHoraFimSab(Time horaFimSab) {
        this.horaFimSab = horaFimSab;
    }
}
