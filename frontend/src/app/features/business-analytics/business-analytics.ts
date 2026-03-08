import {
  Component,
  signal,
  inject,
  OnInit,
  AfterViewInit,
  ViewChild,
  ElementRef
} from '@angular/core';
 
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Navbar } from '../navbar/navbar';
import { Chart, registerables } from 'chart.js';
 
Chart.register(...registerables);
 
@Component({
  selector: 'app-business-analytics',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './business-analytics.html',
  styleUrls: ['./business-analytics.css']
})
export class BusinessAnalytics implements OnInit, AfterViewInit {
 
  private http = inject(HttpClient);
 
  @ViewChild('growthChart') growthChartRef!: ElementRef;
  @ViewChild('engagementChart') engagementChartRef!: ElementRef;
 
  selectedDays = 30;
  dayOptions = [7, 30, 90];
 
  stats = signal<any>(null);
  growthChart: any;
engagementChart: any;
 
  ngOnInit() {
    this.loadAnalytics();
  }
 
  ngAfterViewInit() {
    setTimeout(() => {
      this.renderCharts();
    }, 500);
  }
 
  selectDays(days: number) {
    this.selectedDays = days;
    this.loadAnalytics();
  }
 
 loadAnalytics() {
  this.http.get<any>('http://localhost:8080/api/business/analytics')
    .subscribe(data => {
 
      this.stats.set(data);
 
      this.createGrowthChart(data.monthlyGrowth);
      this.createEngagementChart(
        data.postEngagement,
        data.profileViews
      );
 
    });
}
createGrowthChart(monthlyData: number[]) {
 
  if (this.growthChart) {
    this.growthChart.destroy();
  }
 
  this.growthChart = new Chart('growthChart', {
    type: 'line',
    data: {
      labels: ['Jan','Feb','Mar','Apr','May','Jun'],
      datasets: [{
        label: 'Growth',
        data: monthlyData,
        borderColor: '#3b82f6',
        fill: false,
        tension: 0.4
      }]
    }
  });
}
createEngagementChart(totalEngagement: number, profileViews: number) {
 
  if (this.engagementChart) {
    this.engagementChart.destroy();
  }
 
  this.engagementChart = new Chart('engagementChart', {
    type: 'bar',
    data: {
      labels: ['Engagement','Profile Views'],
      datasets: [{
        label: 'Analytics',
        data: [totalEngagement, profileViews],
        backgroundColor: ['#ef4444','#10b981']
      }]
    }
  });
}
 
  renderCharts() {
 
    if (!this.growthChartRef) return;
 
    new Chart(this.growthChartRef.nativeElement, {
      type: 'line',
      data: {
        labels: ['Jan','Feb','Mar','Apr','May','Jun'],
        datasets: [{
          label: 'Growth',
          data: this.stats().monthlyGrowth,
          borderColor: '#3b82f6',
          tension: 0.4
        }]
      }
    });
 
    new Chart(this.engagementChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Likes','Comments'],
        datasets: [{
          label: 'Engagement',
          data: [
            this.stats().postEngagement / 2,
            this.stats().postEngagement / 2
          ],
          backgroundColor: ['#ef4444', '#f59e0b']
        }]
      }
    });
  }
}