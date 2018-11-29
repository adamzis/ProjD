import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
 
import { DashboardComponent } from './dashboard/dashboard.component';
import { PrimeComponent } from './prime/prime.component';
import { SisComponent } from './sis/sis.component';
 
const routes: Routes = 
[
  { path: '', redirectTo: 'dash', pathMatch: 'full' },
  { path: 'dash', component: DashboardComponent },
  { path: 'prime', component: PrimeComponent },
  { path: 'sis', component: SisComponent}
];
 
@NgModule
({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
