import {
  Component,
  OnInit,
  ViewChild,
  ElementRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Navbar } from '../navbar/navbar';
import { Chart, registerables } from 'chart.js';
import { forkJoin } from 'rxjs';
import { AnalyticsService } from './analytics.service';
import { AnalyticsSummary, TopPostDTO } from './analytics.models';

Chart.register(...registerables);

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule, Navbar],
  templateUrl: './analytics.html',
  styleUrls: ['./analytics.css']
})
export class Analytics implements OnInit {

  summary!: AnalyticsSummary;
  topPosts: TopPostDTO[] = [];
  isLoading = false;
  selectedDays = 30;

  
  hasWeeklyData = false;
  hasFollowerData = false;
  hasReachData = false;

  @ViewChild('weeklyChart') weeklyChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('followerChart') followerChartRef!: ElementRef<HTMLCanvasElement>;
  @ViewChild('reachChart') reachChartRef!: ElementRef<HTMLCanvasElement>;

  weeklyChart!: Chart;
  followerChart!: Chart;
  reachChart!: Chart;

  constructor(private analyticsService: AnalyticsService) {}

  ngOnInit(): void {
    this.loadAllData();
  }

  changeDays(days: number) {
    this.selectedDays = days;
    this.loadAllData();
  }

  loadAllData() {
    this.isLoading = true;

    forkJoin({
      summary: this.analyticsService.getSummary(this.selectedDays),
      weekly: this.analyticsService.getWeeklyEngagement(this.selectedDays),
      followers: this.analyticsService.getFollowerGrowth(this.selectedDays),
      reach: this.analyticsService.getReachImpressions(this.selectedDays),
      topPosts: this.analyticsService.getTopPosts(this.selectedDays)
    }).subscribe({
      next: (data) => {

        this.summary = data.summary;
        this.topPosts = data.topPosts;

        // Check if data exists
        this.hasWeeklyData = data.weekly && data.weekly.length > 0;
        this.hasFollowerData = data.followers && data.followers.length > 0;
        this.hasReachData = data.reach && data.reach.length > 0;

        setTimeout(() => {

          if (this.hasWeeklyData) {
            this.renderWeekly(data.weekly);
          } else {
            this.destroyChart(this.weeklyChart);
          }

          if (this.hasFollowerData) {
            this.renderFollowers(data.followers);
          } else {
            this.destroyChart(this.followerChart);
          }

          if (this.hasReachData) {
            this.renderReach(data.reach);
          } else {
            this.destroyChart(this.reachChart);
          }

        });

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Analytics error:', err);
        this.isLoading = false;
      }
    });
  }

  
  destroyChart(chart: Chart | undefined) {
    if (chart) {
      chart.destroy();
    }
  }

  renderWeekly(data: any[]) {

    this.destroyChart(this.weeklyChart);

    if (!this.weeklyChartRef) return;

    this.weeklyChart = new Chart(this.weeklyChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: data.map(d => d.day),
        datasets: [
          {
            label: 'Likes',
            data: data.map(d => d.likes),
            backgroundColor: '#ef4444'
          },
          {
            label: 'Comments',
            data: data.map(d => d.comments),
            backgroundColor: '#f59e0b'
          },
          {
            label: 'Shares',
            data: data.map(d => d.shares),
            backgroundColor: '#22c55e'
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }

  renderFollowers(data: any[]) {

    this.destroyChart(this.followerChart);

    if (!this.followerChartRef) return;

    this.followerChart = new Chart(this.followerChartRef.nativeElement, {
      type: 'line',
      data: {
        labels: data.map(d => d.date),
        datasets: [{
          label: 'New Followers',
          data: data.map(d => d.newFollowers),
          borderColor: '#2563eb',
          backgroundColor: 'rgba(37,99,235,0.2)',
          fill: true,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }

  renderReach(data: any[]) {

    this.destroyChart(this.reachChart);

    if (!this.reachChartRef) return;

    this.reachChart = new Chart(this.reachChartRef.nativeElement, {
      type: 'line',
      data: {
        labels: data.map(d => d.date),
        datasets: [
          {
            label: 'Reach',
            data: data.map(d => d.reach),
            borderColor: '#8b5cf6'
          },
          {
            label: 'Impressions',
            data: data.map(d => d.impressions),
            borderColor: '#06b6d4'
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }
}