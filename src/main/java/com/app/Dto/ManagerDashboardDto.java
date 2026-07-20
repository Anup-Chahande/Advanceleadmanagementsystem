package com.app.Dto;
public class ManagerDashboardDto {

    private int totalEmployees;
    private int totalLeads;
    private int newLeads;
    private int contacted;
    private int followup;
    private int won;
    private int lost;
	public int getTotalEmployees() {
		return totalEmployees;
	}
	public void setTotalEmployees(int totalEmployees) {
		this.totalEmployees = totalEmployees;
	}
	public int getTotalLeads() {
		return totalLeads;
	}
	public void setTotalLeads(int totalLeads) {
		this.totalLeads = totalLeads;
	}
	public int getNewLeads() {
		return newLeads;
	}
	public void setNewLeads(int newLeads) {
		this.newLeads = newLeads;
	}
	public int getContacted() {
		return contacted;
	}
	public void setContacted(int contacted) {
		this.contacted = contacted;
	}
	public int getFollowup() {
		return followup;
	}
	public void setFollowup(int followup) {
		this.followup = followup;
	}
	public int getWon() {
		return won;
	}
	public void setWon(int won) {
		this.won = won;
	}
	public int getLost() {
		return lost;
	}
	public void setLost(int lost) {
		this.lost = lost;
	}

    // getters and setters
    
    
}