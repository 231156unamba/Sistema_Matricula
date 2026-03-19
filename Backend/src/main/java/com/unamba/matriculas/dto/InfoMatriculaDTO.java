package com.unamba.matriculas.dto;

import java.util.List;

public class InfoMatriculaDTO {
    private List<CursoDisponibleDTO> cursosDisponibles;
    private int creditosMinimos;
    private int creditosMaximos;
    private Double promedioSemestreAnterior; // null si no hay historial
    private boolean tieneJaladoDosVeces;    // si algún curso fue jalado 2+ veces → límite 14

    public InfoMatriculaDTO() {}

    public List<CursoDisponibleDTO> getCursosDisponibles() { return cursosDisponibles; }
    public void setCursosDisponibles(List<CursoDisponibleDTO> cursosDisponibles) { this.cursosDisponibles = cursosDisponibles; }
    public int getCreditosMinimos() { return creditosMinimos; }
    public void setCreditosMinimos(int creditosMinimos) { this.creditosMinimos = creditosMinimos; }
    public int getCreditosMaximos() { return creditosMaximos; }
    public void setCreditosMaximos(int creditosMaximos) { this.creditosMaximos = creditosMaximos; }
    public Double getPromedioSemestreAnterior() { return promedioSemestreAnterior; }
    public void setPromedioSemestreAnterior(Double promedioSemestreAnterior) { this.promedioSemestreAnterior = promedioSemestreAnterior; }
    public boolean isTieneJaladoDosVeces() { return tieneJaladoDosVeces; }
    public void setTieneJaladoDosVeces(boolean tieneJaladoDosVeces) { this.tieneJaladoDosVeces = tieneJaladoDosVeces; }
}
